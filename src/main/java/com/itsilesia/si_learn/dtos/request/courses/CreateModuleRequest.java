package com.itsilesia.si_learn.dtos.request.courses;

import jakarta.validation.constraints.NotBlank;

public record CreateModuleRequest(
	@NotBlank String title
) {
}