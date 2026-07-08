package com.itsilesia.si_learn.dtos.request.courses;

import com.itsilesia.si_learn.entities.enums.DifficultyLevel;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCourseRequest(
	@NotNull @Size(min = 1, max = 255) String title,
	@NotNull @Size(min = 1, max = 4 << 10) String description,
	@NotBlank String languageCode,
	@NotNull DifficultyLevel difficulty,
	@NotNull ProgramingLanguage programingLanguage,
	Boolean forceSequentialTasks
) {

}
