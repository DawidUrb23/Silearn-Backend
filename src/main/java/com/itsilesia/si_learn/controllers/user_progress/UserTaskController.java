package com.itsilesia.si_learn.controllers.user_progress;


import com.itsilesia.si_learn.dtos.request.compiler.SubmitExecuteTaskRequest;
import com.itsilesia.si_learn.dtos.response.CompilationResponse;
import com.itsilesia.si_learn.dtos.response.user_progress.SubmitTestTaskRequest;
import com.itsilesia.si_learn.dtos.response.user_progress.TaskSubmitResponse;
import com.itsilesia.si_learn.dtos.response.user_progress.TaskUserWithContentsResponse;
import com.itsilesia.si_learn.services.user_progress.UserTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/my/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "My Tasks", description = "manage tasks that user sent and teacher can check")
public class UserTaskController {

	private final UserTaskService userTaskService;

	@GetMapping("/{taskId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Shows full user info about task")
	public TaskUserWithContentsResponse getTaskUser(
		@PathVariable UUID taskId,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId,
		@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String languageCode   //exposes the header for LocaleContextHolder
	) {
		return TaskUserWithContentsResponse.fromEntity(
			userTaskService.getTaskUser(taskId, userId));
	}

	@PostMapping("{taskId}/run")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Operation(summary = "Sends user code and std::in to API to execute it", description = "CompilationId is returned from which the user can check his result")
	public UUID onlineCompilerRun(
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId,
		@PathVariable UUID taskId,
		@Valid @RequestBody SubmitExecuteTaskRequest request) {
		return userTaskService.runStudentCode(taskId, userId, request);
	}

	@PostMapping("{taskId}/submit")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Operation(summary = "Sends user code to api to test it (automatically or submit it for manual grading)")
	public TaskSubmitResponse onlineCompilerTests(
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId,
		@PathVariable UUID taskId,
		@RequestBody SubmitTestTaskRequest submitTestTaskRequest) {
		return userTaskService.testStudentCodeWithTestScenario(taskId, userId, submitTestTaskRequest);
	}

	@GetMapping("compilation/{compilationId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get Compilation Details")
	public CompilationResponse getCompilationDetails(
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId,
		@PathVariable UUID compilationId) {
		return userTaskService.getCompilationDetails(userId, compilationId);
	}
}