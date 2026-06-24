package com.itsilesia.si_learn.dtos.response.helper;

import com.itsilesia.si_learn.entities.DictionaryItem;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import io.micrometer.common.util.StringUtils;

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
		TranslationDictionary dictionary,
		String languageCode
	) {
		if (StringUtils.isBlank(languageCode))
			return getOriginalTranslation(dictionary);
		return dictionary.getItems().stream()
			.filter(item ->
				item.getLanguage().getCode().equals(languageCode)
			)
			.findFirst()
			.orElseGet(() -> getOriginalTranslation(dictionary));
	}
}
