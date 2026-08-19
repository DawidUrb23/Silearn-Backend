package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.dtos.response.dictonary.LanguageDto;
import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDto;

import java.util.List;

public record KeysetTranslationDictionaryResponse(
	List<TranslationDto> data,
	List<LanguageDto> languages,
	String nextCursor
) {
}
