package com.itsilesia.si_learn.dtos.request.admin;

import com.itsilesia.si_learn.config.ValidationConstants;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramingLanguageCompilerUpsertRequest {
	private ProgramingLanguage language;

	@Size(max = ValidationConstants.programingLanguageCompilerLenMax)
	private String compiler;
	
	@Size(max = ValidationConstants.programingLanguageCompilerLenMax)
	private String compilerNamePrefix;
}
