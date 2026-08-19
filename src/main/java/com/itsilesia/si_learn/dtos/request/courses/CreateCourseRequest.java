package com.itsilesia.si_learn.dtos.request.courses;

import com.itsilesia.si_learn.config.ValidationConstants;
import com.itsilesia.si_learn.entities.enums.DifficultyLevel;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.validators.locale.ValidLocaleCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Locale;

public record CreateCourseRequest(
	@NotNull
	@Size(min = ValidationConstants.titleLenMin, max = ValidationConstants.titleLenMax)
	String title,

	@NotNull
	@Size(min = ValidationConstants.descriptionLenMin, max = ValidationConstants.descriptionLenMax)
	String description,

	@ValidLocaleCode
	Locale languageCode,

	@NotNull
	DifficultyLevel difficulty,

	@NotNull
	ProgramingLanguage programingLanguage,

	Boolean forceSequentialTasks
) {
}
