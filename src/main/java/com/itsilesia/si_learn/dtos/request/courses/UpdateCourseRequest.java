package com.itsilesia.si_learn.dtos.request.courses;

import com.itsilesia.si_learn.config.ValidationConstants;
import com.itsilesia.si_learn.entities.enums.DifficultyLevel;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.entities.enums.VisibilityStatus;
import jakarta.validation.constraints.Size;

public record UpdateCourseRequest(
	@Size(min = ValidationConstants.titleLenMin, max = ValidationConstants.titleLenMax)
	String title,

	@Size(min = ValidationConstants.descriptionLenMin, max = ValidationConstants.descriptionLenMax)
	String description,

	DifficultyLevel difficulty,

	ProgramingLanguage programingLanguage,

	Boolean forceSequentialTasks,

	VisibilityStatus status
) {
}
