package com.itsilesia.si_learn.dtos.request.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTranslationDictionaryRequest {
	private String languageCode;
	private String content;
}
