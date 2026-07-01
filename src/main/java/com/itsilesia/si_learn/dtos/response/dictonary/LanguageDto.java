package com.itsilesia.si_learn.dtos.response.dictonary;

import com.itsilesia.si_learn.entities.Language;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public record LanguageDto(
	String code,
	String name,
	String nativeName
) {
	static public LanguageDto fromEntity(Language language) {
		Locale currentLocale = LocaleContextHolder.getLocale(); //the Locale in which to display the `name` field populated automagically by providing HttpHeaders.ACCEPT_LANGUAGE
		return new LanguageDto(
			language.getCode(),
			language.getLocale().getDisplayName(currentLocale),
			language.getLocale().getDisplayLanguage(language.getLocale())
		);
	}
}
