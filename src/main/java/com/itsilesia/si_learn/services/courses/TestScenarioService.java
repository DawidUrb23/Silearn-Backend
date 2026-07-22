package com.itsilesia.si_learn.services.courses;

import com.itsilesia.si_learn.dtos.request.courses.TestScenarioRequest;
import com.itsilesia.si_learn.entities.courses.TestScenario;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.courses.TestScenarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestScenarioService {

	private final TestScenarioRepository testScenarioRepository;
	private final CourseHelperService courseHelperService;

	public TestScenario getTestScenarioById(UUID id) {
		return testScenarioRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(TestScenario.class, id));
	}

	@Transactional
	public void deleteTestScenario(UUID id, UUID userId) {
		TestScenario testScenario = testScenarioRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(TestScenario.class, id));
		courseHelperService.isAuthorOrThrow(testScenario, userId);

		testScenarioRepository.delete(testScenario);
	}

	public TestScenario updateTestScenario(UUID id, TestScenarioRequest testScenarioRequest, UUID userId) {
		TestScenario testScenario = testScenarioRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(TestScenario.class, id));
		courseHelperService.isAuthorOrThrow(testScenario, userId);

		if (testScenarioRequest.getExpectedOutput() != null && !testScenarioRequest.getExpectedOutput().isEmpty()) {
			testScenario.setExpectedOutput(testScenarioRequest.getExpectedOutput());
		}
		if (testScenarioRequest.getInput() != null && !testScenarioRequest.getInput().isEmpty()) {
			testScenario.setInput(testScenarioRequest.getInput());
		}
		if (testScenarioRequest.getTimeoutSec() != null) {
			testScenario.setTimeoutSec(testScenarioRequest.getTimeoutSec());
		}
		return testScenarioRepository.save(testScenario);
	}

}
