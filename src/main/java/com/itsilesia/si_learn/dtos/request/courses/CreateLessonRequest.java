package com.itsilesia.si_learn.dtos.request.courses;

import jakarta.validation.constraints.NotBlank;

public record CreateLessonRequest(
	@NotBlank String topic,
	@NotBlank String description
) {
}
