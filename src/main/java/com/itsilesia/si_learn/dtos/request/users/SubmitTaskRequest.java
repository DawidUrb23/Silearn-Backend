package com.itsilesia.si_learn.dtos.request.users;

import jakarta.validation.constraints.NotBlank;

public record SubmitTaskRequest(
	@NotBlank String code
) {
}

