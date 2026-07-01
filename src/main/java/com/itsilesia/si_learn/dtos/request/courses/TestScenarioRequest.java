package com.itsilesia.si_learn.dtos.request.courses;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TestScenarioRequest {
	@Size(max = 4 << 10)
	private String input;

	@Min(0)
	private Float timeoutSec;

	@Size(max = 4 << 10)
	private String expectedOutput;
}
