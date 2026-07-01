package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDto;
import com.itsilesia.si_learn.dtos.response.helper.DictionaryHelper;
import com.itsilesia.si_learn.entities.courses.Module;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ModuleResponse(
	UUID id,
	TranslationDto title,
	Integer index
) {
	public static ModuleResponse fromEntity(@NotNull Module module) {
		return new ModuleResponse(
			module.getId(),
			TranslationDto.fromEntity(
				DictionaryHelper.getTranslationWithCode(module.getTitle())
			),
			module.getIndex()
		);
	}
}

