package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.courses.TestScenarioRequest;
import com.itsilesia.si_learn.dtos.response.courses.TestScenarioResponse;
import com.itsilesia.si_learn.entities.courses.TestScenario;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.TaskRepository;
import com.itsilesia.si_learn.repositories.TestScenarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestScenarioService {

	private final TestScenarioRepository testScenarioRepository;
	private final TaskRepository taskRepository;

	public TestScenarioResponse getTestScenarioById(UUID id) {
		TestScenario testScenario = testScenarioRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(TestScenario.class, id));
		return TestScenarioResponse.mapTestScenarioDto(testScenario);
	}

	@Transactional
	public void deleteTestScenario(UUID id) {
		TestScenario testScenario = testScenarioRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(TestScenario.class, id));

		testScenarioRepository.delete(testScenario);
	}

	public TestScenarioResponse updateTestScenario(UUID id, TestScenarioRequest testScenarioRequest) {
		TestScenario testScenario = testScenarioRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(TestScenario.class, id));
		if (testScenarioRequest.getExpectedOutput() != null && !testScenarioRequest.getExpectedOutput().isEmpty()) {
			testScenario.setExpectedOutput(testScenarioRequest.getExpectedOutput());
		}
		if (testScenarioRequest.getInput() != null && !testScenarioRequest.getInput().isEmpty()) {
			testScenario.setInput(testScenarioRequest.getInput());
		}
		if (testScenarioRequest.getTimeoutSec() != null) {
			testScenario.setTimeoutSec(testScenarioRequest.getTimeoutSec());
		}
		TestScenario testScenarioSaved = testScenarioRepository.save(testScenario);
		return TestScenarioResponse.mapTestScenarioDto(testScenarioSaved);

	}

	public List<TestScenarioResponse> getTestScenarioByTaskId(UUID taskId) {
		List<TestScenario> testScenarios = testScenarioRepository.findAllByTaskId(taskId);

		if (testScenarios.isEmpty()) {
			return List.of();
		}
		return testScenarios.stream()
			.map(TestScenarioResponse::mapTestScenarioDto)
			.collect(Collectors.toList());
	}
}
