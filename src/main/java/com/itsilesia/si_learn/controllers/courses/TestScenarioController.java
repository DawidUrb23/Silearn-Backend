package com.itsilesia.si_learn.controllers.courses;

import com.itsilesia.si_learn.dtos.request.courses.TestScenarioRequest;
import com.itsilesia.si_learn.dtos.response.courses.TestScenarioResponse;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.services.courses.CourseHelperService;
import com.itsilesia.si_learn.services.courses.TestScenarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/test-scenarios")
@RequiredArgsConstructor
@Validated
@Tag(name = "Test Scenarios", description = "manage Test Scenarios (Owner Teacher)")
public class TestScenarioController {

	private final TestScenarioService testScenarioService;
	private final CourseHelperService courseHelperService;

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get test scenario by id")
	@ApiResponse(responseCode = "403", description = "Not the owner!",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public ResponseEntity<TestScenarioResponse> getTestScenarioById(
		@PathVariable UUID id,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		var scenario = testScenarioService.getTestScenarioById(id);
		courseHelperService.isAuthorOrThrow(scenario.getTask(), userId);
		return new ResponseEntity<>(TestScenarioResponse.fromEntity(scenario), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete test scenario by id")
	@ApiResponse(responseCode = "403", description = "Not the owner!",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void deleteTestScenario(
		@PathVariable UUID id,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		testScenarioService.deleteTestScenario(id, userId);
	}

	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Update test scenario by id")
	@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "403", description = "Not the owner!",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void updateTestScenario(
		@PathVariable UUID id,
		@RequestBody @Valid TestScenarioRequest testScenarioRequest,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		testScenarioService.updateTestScenario(id, testScenarioRequest, userId);
	}

}
