package com.itsilesia.si_learn.dtos.response.helper;

import com.itsilesia.si_learn.entities.DictionaryItem;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public final class DictionaryHelper {
	private DictionaryHelper() {
	}

	public static DictionaryItem getOriginalTranslation(TranslationDictionary dictionary) {
		return dictionary.getItems().stream()
			.filter(item ->
				item.getLanguage().getCode().equals(dictionary.getOriginalLanguage().getCode())
			)
			.findFirst()
			.orElse(null);
	}

	public static DictionaryItem getTranslationWithCode(
		TranslationDictionary dictionary
	) {
		Locale currentLocale = LocaleContextHolder.getLocale(); //the Locale in which to display the `name` field populated automagically by providing HttpHeaders.ACCEPT_LANGUAGE
		return dictionary.getItems().stream()
			.filter(item ->
				item.getLanguage().getCode().equals(currentLocale.getLanguage())
			)
			.findFirst()
			.orElseGet(() -> getOriginalTranslation(dictionary));
	}
}
