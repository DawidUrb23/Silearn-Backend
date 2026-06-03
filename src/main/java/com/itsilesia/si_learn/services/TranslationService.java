package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.entities.DictionaryItem;
import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.LanguageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationService {
	private final LanguageRepository languageRepository;

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
}
