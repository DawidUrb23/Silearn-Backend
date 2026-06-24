package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.courses.TestScenarioRequest;
import com.itsilesia.si_learn.dtos.response.courses.TestScenarioResponse;
import com.itsilesia.si_learn.services.TestScenarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/test-scenarios")
@RequiredArgsConstructor
public class TestScenarioController {

	private final TestScenarioService testScenarioService;

	@GetMapping("/{id}")
	public ResponseEntity<TestScenarioResponse> getTestScenarioById(@PathVariable UUID id) {
		TestScenarioResponse response = testScenarioService.getTestScenarioById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTestScenario(@PathVariable UUID id) {
		testScenarioService.deleteTestScenario(id);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<TestScenarioResponse> updateTestScenario(@PathVariable UUID id, @RequestBody TestScenarioRequest testScenarioRequest) {
		TestScenarioResponse response = testScenarioService.updateTestScenario(id, testScenarioRequest);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
