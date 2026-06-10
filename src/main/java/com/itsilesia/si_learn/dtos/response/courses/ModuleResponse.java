package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.DictionaryItem;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.entities.courses.Module;

import java.util.UUID;

public record ModuleResponse(
	UUID id,
	String title,
	Integer index
) {
	public static ModuleResponse fromEntity(Module module) {
		return new ModuleResponse(
			module.getId(),
			getOriginalValue(module.getTitle()),
			module.getIndex()
		);
	}

	private static String getOriginalValue(TranslationDictionary dictionary) {
		return dictionary.getItems().stream()
			.filter(item -> item.getLanguage().getCode().equals(dictionary.getOriginalLanguage().getCode()))
			.findFirst()
			.map(DictionaryItem::getValue)
			.orElse(null);
	}
}

