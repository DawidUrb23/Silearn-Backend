package com.itsilesia.si_learn.dtos.response.dictonary;

import com.itsilesia.si_learn.entities.DictionaryItem;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TranslationDictDto {
	private String originalLanguage;
	private Map<String, String> translations;

	static public TranslationDictDto fromEntity(TranslationDictionary dictionary) {
		return new TranslationDictDto(
			dictionary.getOriginalLanguage().getCode(),
			dictionary.getItems().stream()
				.collect(Collectors.toMap(
					item -> item.getLanguage().getCode(),
					DictionaryItem::getValue
				))
		);
	}
}
