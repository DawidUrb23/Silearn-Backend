package com.itsilesia.si_learn.dtos.request.courses;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateModuleRequest(
	@NotNull @Size(min = 1, max = 255) String title
) {
}