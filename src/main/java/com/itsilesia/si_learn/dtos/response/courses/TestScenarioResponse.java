package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.courses.TestScenario;
import lombok.*;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TestScenarioResponse {

	private UUID id;

	private UUID taskId;

	private String input;

	private String expectedOutput;

	private Float timeoutSec;

	public static TestScenarioResponse mapTestScenarioDto(TestScenario testScenario) {
		if (testScenario == null) {
			return null;
		}

		return TestScenarioResponse.builder()
			.input(testScenario.getInput())
			.expectedOutput(testScenario.getExpectedOutput())
			.timeoutSec(testScenario.getTimeoutSec())
			.id(testScenario.getId())
			.taskId(testScenario.getTask() != null ? testScenario.getTask().getId() : null)
			.build();

	}
}
