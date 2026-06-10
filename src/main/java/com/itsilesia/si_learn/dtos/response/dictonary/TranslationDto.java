package com.itsilesia.si_learn.dtos.response.dictonary;

import com.itsilesia.si_learn.entities.DictionaryItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TranslationDto {
	private String languageCode;
	private String content;

	static public TranslationDto fromEntity(DictionaryItem item) {
		return new TranslationDto(
			item.getLanguage().getCode(),
			item.getValue()
		);
	}
}
