package com.itsilesia.si_learn.dtos.request.users;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GradeTaskRequest(
	@Min(0) @Max(100) Float grade
) {
}
