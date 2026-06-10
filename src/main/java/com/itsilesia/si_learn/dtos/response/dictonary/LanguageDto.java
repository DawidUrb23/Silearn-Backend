package com.itsilesia.si_learn.dtos.response.dictonary;

import com.itsilesia.si_learn.entities.Language;

import java.util.Locale;

public record LanguageDto(
	String code,
	String name,
	String nativeName
) {
	static public LanguageDto fromEntity(Language language, Locale currentLocale) {
		return new LanguageDto(
			language.getCode(),
			language.getLocale().getDisplayName(currentLocale),
			language.getLocale().getDisplayLanguage(language.getLocale())
		);
	}
}
