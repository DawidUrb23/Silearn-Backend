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

	public TestScenarioResponse(TestScenario testScenario) {
		this.id = testScenario.getId();
		this.taskId = testScenario.getTask() != null ? testScenario.getTask().getId() : null;
		this.input = testScenario.getInput();
		this.expectedOutput = testScenario.getExpectedOutput();
		this.timeoutSec = testScenario.getTimeoutSec();
	}

	public static TestScenarioResponse fromEntity(TestScenario testScenario) {
		return testScenario == null ? null : new TestScenarioResponse(testScenario);
	}
}
