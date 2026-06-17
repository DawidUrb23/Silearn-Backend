package com.itsilesia.si_learn.dtos.request.courses;

import com.itsilesia.si_learn.entities.enums.DifficultyLevel;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.entities.enums.VisibilityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCourseRequest(
	@NotBlank String title,
	@NotBlank String description,
	@NotBlank String languageCode,
	@NotNull VisibilityStatus status,
	@NotNull DifficultyLevel difficulty,
	@NotNull ProgramingLanguage programingLanguage,
	boolean forceSequentialTasks
) {

}
