package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDto;
import com.itsilesia.si_learn.dtos.response.helper.DictionaryHelper;
import com.itsilesia.si_learn.entities.courses.Module;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ModuleResponse {
	private UUID id;
	private TranslationDto title;

	public ModuleResponse(@NotNull Module module) {
		this.id = module.getId();
		this.title = TranslationDto.fromEntity(
			DictionaryHelper.getTranslationWithCode(module.getTitle())
		);
	}

	public static ModuleResponse fromEntity(@NotNull Module module) {
		return new ModuleResponse(module);
	}
}

