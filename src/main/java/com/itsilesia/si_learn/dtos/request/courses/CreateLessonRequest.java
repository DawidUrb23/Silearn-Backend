package com.itsilesia.si_learn.dtos.request.courses;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateLessonRequest(
	@NotNull @Size(min = 1, max = 255) String topic,
	@NotNull @Size(min = 1, max = 4 << 10) String description
) {
}
