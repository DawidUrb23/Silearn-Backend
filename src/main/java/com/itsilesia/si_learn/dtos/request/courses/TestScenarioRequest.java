package com.itsilesia.si_learn.dtos.request.courses;

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
	@Size(max = 4 * 1024)
	private String input;

	private Float timeoutSec;

	@Size(max = 4 * 1024)
	private String expectedOutput;
}
