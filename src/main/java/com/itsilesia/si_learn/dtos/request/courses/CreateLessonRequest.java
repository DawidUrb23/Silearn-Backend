package com.itsilesia.si_learn.dtos.request.courses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLessonRequest(
	@NotBlank String topic,
	@NotBlank String description,
	@NotNull Integer index
) {
}
