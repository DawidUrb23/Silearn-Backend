package com.itsilesia.si_learn.dtos.request.courses;

import com.itsilesia.si_learn.config.ValidationConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateModuleRequest(
	@NotNull
	@Size(min = ValidationConstants.titleLenMin, max = ValidationConstants.titleLenMax)
	String title
) {
}