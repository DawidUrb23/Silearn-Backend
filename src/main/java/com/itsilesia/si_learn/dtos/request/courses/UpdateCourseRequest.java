package com.itsilesia.si_learn.dtos.request.courses;

import com.itsilesia.si_learn.entities.enums.DifficultyLevel;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.entities.enums.VisibilityStatus;
import jakarta.validation.constraints.Size;

public record UpdateCourseRequest(
	@Size(min = 1, max = 255)
	String title,

	@Size(min = 1, max = 4 << 10)
	String description,

	DifficultyLevel difficulty,

	ProgramingLanguage programingLanguage,

	Boolean forceSequentialTasks,

	VisibilityStatus status
) {
}
