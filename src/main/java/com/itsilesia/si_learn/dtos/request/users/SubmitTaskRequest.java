package com.itsilesia.si_learn.dtos.request.users;

import com.itsilesia.si_learn.config.ValidationConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record SubmitTaskRequest(
	@NotBlank
	@Max(ValidationConstants.codeLenMax)
	String code
) {
}

