package com.itsilesia.si_learn.dtos.request.courses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateModuleRequest(
	@NotBlank String title,
	@NotBlank String languageCode,
	@NotNull Integer index
) {
}