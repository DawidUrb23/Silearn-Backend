package com.itsilesia.si_learn.dtos.response.admin;

import com.itsilesia.si_learn.entities.ProgramingLanguageCompiler;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProgramingLanguageCompilerResponse {
	private ProgramingLanguage language;
	private String compiler;
	private String compilerNamePrefix;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public ProgramingLanguageCompilerResponse(@NotNull ProgramingLanguageCompiler programingLanguageCompiler) {
		this.compiler = programingLanguageCompiler.getCompiler();
		this.language = programingLanguageCompiler.getLanguage();
		this.compilerNamePrefix = programingLanguageCompiler.getCompilerNamePrefix();
		this.createdAt = programingLanguageCompiler.getCreatedAt();
		this.updatedAt = programingLanguageCompiler.getUpdatedAt();
	}

	public static ProgramingLanguageCompilerResponse FromEntity(@NotNull ProgramingLanguageCompiler programingLanguageCompiler) {
		return new ProgramingLanguageCompilerResponse(programingLanguageCompiler);
	}
}
