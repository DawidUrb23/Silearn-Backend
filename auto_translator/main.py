from langchain.tools import tool
from langchain_ollama import ChatOllama
from langgraph.checkpoint.memory import InMemorySaver
from langchain_core.messages import HumanMessage, AIMessage, ToolMessage, AIMessageChunk, SystemMessage
from langchain.agents import create_agent
import json
import textwrap
import ast
import requests
import urllib3

# Disable warnings for self-signed certificates
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

# =========================================================
# CONFIG
# =========================================================
TRANSLATE_MODEL_NAME = "translategemma"
TRANSLATE_TEMPERATURE = 0

DRIVER_MODEL_NAME = "qwen3.5:4b"
DRIVER_TEMPERATURE = 0.2
DRIVER_REASONING = False  # Set this to False if you want to disable <think> blocks

API_PATH = "http://localhost:8080/api"
API_TRANSLATIONS = "translations"
API_NOT_TRANSLATED = "not-translated"
API_TOKEN = ""

QUERY_INCLUDE = {
    "cursor": "",
    "size": 10,
    "autoTranslatable": True,
    "courseId": None
}

ai_msg_pattern = """
For context the content is for programing education platform.
Translate this "%input_language%" language code text block (in markup) to "%output_language%" language code.
Return only the translation, keep the code blocks untranslated, there is only one file so do not add the "---File" marks.
"""

# =========================================================
# TOOLS
# =========================================================

@tool
def SetApiToken(token: str) -> str:
    """Sets the API auth token required to connect to the API."""
    global API_TOKEN
    API_TOKEN = "Bearer " + token
    return "API Token successfully set."

def BuildFetchUri(cursor: str) -> str:
    # Build URL with the query path
    return f"{API_PATH.rstrip('/')}/{API_TRANSLATIONS}/{API_NOT_TRANSLATED}"

def BuildPostUri(id: str) -> str:
    return f"{API_PATH.rstrip('/')}/{API_TRANSLATIONS}/{id}/{API_TRANSLATIONS}"

def BuildQueryParams(cursor: str, courseId: str = None) -> dict:
    params = {
        "size": QUERY_INCLUDE.get("size", 10),
        "autoTranslatable": str(QUERY_INCLUDE.get("autoTranslatable", True)).lower(),
    }
    if cursor:
        params["cursor"] = cursor
    if courseId:
        params["courseId"] = courseId
    return params



@tool
def FetchItemsToTranslate(cursor: str, courseId: str = None) -> dict:
    """Gets a batch of items to translate from the API. Uses the cursor argument to advance to the next batch. Uses optional courseId argument to filter by course."""
    headers = {}
    if API_TOKEN:
        headers["Authorization"] = API_TOKEN
    
    params = BuildQueryParams(cursor)
    url = BuildFetchUri(cursor)
    
    # verify=False is used because localhost might use self-signed certificates
    response = requests.get(url, headers=headers, params=params, verify=False)
    response.raise_for_status()
    return response.json()

# Translates each item to every target language (excluding the original language). using the translate LLM, sends the translation update requests back to the API, and stops when no more items or cursors are left.
@tool
def RunAutoTranslation(courseId: str = None) -> str:
    """Runs the automatic translation process. Translates each item to every target language. Uses optional courseId argument to filter by course."""
    global API_TOKEN
    if not API_TOKEN:
        return "Error: API token not set. Please set the API token using SetApiToken before running auto translation."

    try:
        translate_llm = ChatOllama(model=TRANSLATE_MODEL_NAME, temperature=TRANSLATE_TEMPERATURE)
    except Exception as e:
        return f"Error initializing translation LLM: {e}"

    cursor = QUERY_INCLUDE.get("cursor", "")
    total_translated = 0
    batch_count = 0

    headers = {
        "Authorization": API_TOKEN,
        "Content-Type": "application/json"
    }

    # Fetch and translate loop
    while True:
        batch_count += 1
        print(f"Fetching batch {batch_count} with cursor: '{cursor}'...")
        try:
            params = BuildQueryParams(cursor, courseId)
            fetch_url = BuildFetchUri(cursor)
            response = requests.get(fetch_url, headers=headers, params=params, verify=False)
            response.raise_for_status()
            response_data = response.json()
        except Exception as e:
            return f"Error fetching items to translate (batch {batch_count}): {e}"

        data = response_data.get("data", [])
        languages = response_data.get("languages", [])
        next_cursor = response_data.get("nextCursor")

        if not data:
            print("No more items to translate.")
            break

        print(f"Received {len(data)} items to translate. Available languages: {[l.get('code') for l in languages]}")

        for item in data:
            dict_id = item.get("dictId")
            orig_lang = item.get("languageCode")
            content = item.get("content")

            if not dict_id or not content or not orig_lang:
                continue

            translations_payload = []

            for lang in languages:
                target_lang = lang.get("code")
                if not target_lang or target_lang == orig_lang:
                    continue

                # Prepare safe messages to prevent prompt hijacking
                instructions = ai_msg_pattern.strip().replace('"%input_language%"', f'"{orig_lang}"').replace('"%output_language%"', f'"{target_lang}"')

                messages = [
                    SystemMessage(content=instructions),
                    HumanMessage(content=content)
                ]

                print(f"Translating item {dict_id} from {orig_lang} to {target_lang}...")
                try:
                    translation_response = translate_llm.invoke(messages)
                    translated_text = translation_response.content.strip()
                    
                    translations_payload.append({
                        "languageCode": target_lang,
                        "content": translated_text
                    })
                except Exception as e:
                    print(f"Error during translation of item {dict_id} to {target_lang}: {e}")

            if translations_payload:
                post_url = BuildPostUri(dict_id)
                payload = {
                    "translations": translations_payload
                }
                print(f"Posting translations for item {dict_id} to {post_url}...")
                try:
                    post_response = requests.post(post_url, headers=headers, json=payload, verify=False)
                    post_response.raise_for_status()
                    total_translated += 1
                except Exception as e:
                    print(f"Error posting translations for item {dict_id}: {e}")

        # Advance cursor
        if not next_cursor or next_cursor == cursor:
            break
        cursor = next_cursor

    return f"Auto translation complete. Processed {batch_count} batches. Successfully translated and updated {total_translated} items."

# =========================================================
# DRIVER AGENT & INTERACTIVE LOOP
# =========================================================

SYSTEM_PROMPT = """You are an AI assistant designed to help set up and run the auto-translation tool.
You have tools to:
1. Set the API token: SetApiToken(token)
2. Fetch items to translate (for manual inspection/debugging): FetchItemsToTranslate(cursor, courseId)
3. Run the complete auto-translation loop: RunAutoTranslation(courseId)

First, ask the user to provide the API token. Once provided, call SetApiToken.
Then ask the user if he wants to run the tool without filters or with courseId filter.
Run the tool RunAutoTranslation with courseId filter if defined.
Respond concisely.
"""

def main():
    print(f"\n=== Auto-Translator Setup {DRIVER_MODEL_NAME} ===")
    print(f"    Auto-Translator powered by: {TRANSLATE_MODEL_NAME}")
    print("Type 'exit' or 'quit' to stop.\n")
    
    model_kwargs = {}
    if DRIVER_REASONING:
        model_kwargs["think"] = DRIVER_REASONING
        
    try:
        driver_llm = ChatOllama(
            model=DRIVER_MODEL_NAME,
            temperature=DRIVER_TEMPERATURE,
            model_kwargs=model_kwargs
        )
    except Exception as e:
        print(f"Error initializing ChatOllama model: {e}")
        return

    agent_tools = [SetApiToken, FetchItemsToTranslate, RunAutoTranslation]
    memory = InMemorySaver()
    agent_executor = create_agent(
        driver_llm,
        agent_tools,
        system_prompt=SYSTEM_PROMPT,
        checkpointer=memory
    )
    
    config = {"configurable": {"thread_id": "auto_translator_session"}}
    initial_prompt = "Hello! Please guide me through setting up the API token and running the translator."
    
    def print_agent_stream(prompt_text):
        payload = {"messages": [HumanMessage(content=prompt_text)]}
        full_response = ""
        print("AI: ", end="", flush=True)
        try:
            for msg, metadata in agent_executor.stream(payload, config, stream_mode="messages"):
                if isinstance(msg, AIMessageChunk):
                    full_response += msg.content
                    display_text = full_response
                    if "</think>" in display_text:
                        to_print = display_text.split("</think>")[-1].lstrip('\n')
                        print("\rAI: " + to_print, end="", flush=True)
                    elif "<think>" in display_text:
                        print("\rAI: 🤔 *Thinking...*", end="", flush=True)
                    else:
                        print(msg.content, end="", flush=True)
        except Exception as e:
            print(f"\n[Agent Error during execution: {e}]")
        print("\n")

    print_agent_stream(initial_prompt)

    while True:
        try:
            user_input = input("User >> ").strip()
            if user_input.lower() in ["exit", "quit"]:
                break
            if not user_input:
                continue
            
            print_agent_stream(user_input)
            
        except KeyboardInterrupt:
            break
        except Exception as e:
            print(f"\nError in loop: {e}\n")

if __name__ == "__main__":
    main()
