package com.itsilesia.si_learn.dtos.request.courses;

import com.itsilesia.si_learn.config.ValidationConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateLessonRequest(
	@NotNull
	@Size(min = ValidationConstants.titleLenMin, max = ValidationConstants.titleLenMax)
	String topic,
	@NotNull
	@Size(min = ValidationConstants.descriptionLenMin, max = ValidationConstants.descriptionLenMax)
	String description
) {
}
