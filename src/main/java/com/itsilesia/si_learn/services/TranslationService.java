package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.dictionary.UpdateTranslationDictionaryRequest;
import com.itsilesia.si_learn.entities.DictionaryItem;
import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.exceptions.ItemAlreadyExistsException;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.LanguageRepository;
import com.itsilesia.si_learn.repositories.TranslationDictionaryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranslationService {
	private final LanguageRepository languageRepository;
	private final TranslationDictionaryRepository translationDictionaryRepository;

	@Transactional
	public TranslationDictionary createDictionary(String value, String languageCode) {
		Language language = languageRepository.findById(languageCode)
			.orElseThrow(() -> new ItemNotFoundException(Language.class, languageCode));

		TranslationDictionary dictionary = new TranslationDictionary();
		dictionary.setOriginalLanguage(language);

		DictionaryItem item = new DictionaryItem();
		item.setLanguage(language);
		item.setValue(value);
		item.setTranslationDictionary(dictionary);

		dictionary.getItems().add(item);

		return dictionary;
	}

	public List<Language> getLanguages() {
		return languageRepository.findAll();
	}

	@Transactional
	public Language addLocale(@NotBlank String languageCode) {
		languageCode = languageCode.toLowerCase();
		if (languageRepository.existsById(languageCode))
			throw new ItemAlreadyExistsException(Language.class, languageCode);

		var lang = new Language(languageCode);
		return languageRepository.save(lang);
	}

	public TranslationDictionary getTranslationDictionary(UUID id) {
		return translationDictionaryRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(TranslationDictionary.class, id));
	}

	@Transactional
	public TranslationDictionary updateTranslationDictionary(UUID id, @Valid UpdateTranslationDictionaryRequest request) {
		TranslationDictionary translationDict = translationDictionaryRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(TranslationDictionary.class, id));
		Language language = languageRepository.findById(request.getLanguageCode())
			.orElseThrow(() -> new ItemNotFoundException(Language.class, request.getLanguageCode()));

		//try to update the existing item
		var existingItem = translationDict.getItems().stream()
			.filter(item -> item.getLanguage().getCode().equals(request.getLanguageCode()))
			.findFirst();

		if (existingItem.isPresent()) {
			//update the item
			existingItem.get().setValue(request.getContent());
		} else {
			//add a new one
			var newItem = new DictionaryItem();
			newItem.setLanguage(language);
			newItem.setValue(request.getContent());
			newItem.setTranslationDictionary(translationDict);
			translationDict.getItems().add(newItem);
		}

		return translationDictionaryRepository.save(translationDict);
	}

	public void removeLocale(@NotBlank String languageCode) {
		languageRepository.deleteById(languageCode);
	}
}
