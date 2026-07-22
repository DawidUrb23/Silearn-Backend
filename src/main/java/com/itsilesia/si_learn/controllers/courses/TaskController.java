package com.itsilesia.si_learn.controllers.courses;

import com.itsilesia.si_learn.dtos.request.courses.ContentsOrderUpdateRequest;
import com.itsilesia.si_learn.dtos.request.courses.TaskContentRequest;
import com.itsilesia.si_learn.dtos.request.courses.TestScenarioRequest;
import com.itsilesia.si_learn.dtos.request.courses.UpdateTaskRequest;
import com.itsilesia.si_learn.dtos.response.IdOnlyDto;
import com.itsilesia.si_learn.dtos.response.courses.TaskWithContentResponse;
import com.itsilesia.si_learn.dtos.response.courses.TestScenarioResponse;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.services.courses.CourseHelperService;
import com.itsilesia.si_learn.services.courses.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
@Validated
@Tag(name = "Tasks", description = "manage Tasks (Owner Teacher)")
public class TaskController {
	private final TaskService taskService;
	private final CourseHelperService courseHelperService;

	@GetMapping("/{taskId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get task by id (Owner)")
	@ApiResponse(responseCode = "403", description = "Not the owner!",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public TaskWithContentResponse getTask(
		@PathVariable UUID taskId,
		@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String languageCode,  //exposes the header for LocaleContextHolder
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		Task task = taskService.getTaskById(taskId);
		courseHelperService.isAuthorOrThrow(task, userId);
		return TaskWithContentResponse.fromEntity(task);
	}

	@GetMapping("/{taskId}/test-scenarios")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get test scenarios by task id (Owner)")
	@ApiResponse(responseCode = "403", description = "Not the owner!",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public List<TestScenarioResponse> getTestScenarioByTaskId(
		@PathVariable UUID taskId,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		var task = taskService.getTaskById(taskId);
		courseHelperService.isAuthorOrThrow(task, userId);
		return task.getTestScenarios().stream().map(TestScenarioResponse::fromEntity).toList();
	}

	@DeleteMapping("/{taskId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete task by id (Owner)")
	@ApiResponse(responseCode = "403", description = "Not the owner!",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void deleteTask(
		@PathVariable UUID taskId,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		taskService.deleteTask(taskId, userId);
	}

	@PatchMapping("/{taskId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Update task by id (Owner).")
	@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "403", description = "Not the owner!",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void updateTask(
		@PathVariable UUID taskId,
		@RequestBody @Valid UpdateTaskRequest taskRequest,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		taskService.updateTask(taskRequest, taskId, userId);
	}

	@PostMapping("/{taskId}/contents")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Add content to task (Owner)")
	@ApiResponse(responseCode = "201", description = "returns id of created TaskContent")
	@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "403", description = "Not the owner!",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public IdOnlyDto addContentToTask(
		@PathVariable("taskId") UUID taskId,
		@RequestBody @Valid TaskContentRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		return IdOnlyDto.fromEntity(
			taskService.addTaskContent(taskId, request, userId));
	}

	@PutMapping("/{taskId}/contents")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Update task content order in task (Owner)")
	@ApiResponse(responseCode = "400", description = "Invalid input, contents size mismatch",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "403", description = "Not the owner!",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void UpdateTaskContentOrderInTask(
		@PathVariable("taskId") UUID taskId,
		@RequestBody @Valid ContentsOrderUpdateRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		taskService.updateTaskContentsOrder(taskId, request, userId);
	}

	@PostMapping("/{taskId}/test-scenarios")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Add test scenario to task (Owner)")
	@ApiResponse(responseCode = "201", description = "returns id of created TestScenario")
	@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "403", description = "Not the owner!",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public IdOnlyDto addScenarioToTask(
		@PathVariable("taskId") UUID taskId,
		@RequestBody @Valid TestScenarioRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		return IdOnlyDto.fromEntity(
			taskService.addScenarioToTask(taskId, request, userId)
		);
	}


}
