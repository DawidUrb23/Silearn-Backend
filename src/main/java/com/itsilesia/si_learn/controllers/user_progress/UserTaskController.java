package com.itsilesia.si_learn.controllers.user_progress;


import com.itsilesia.si_learn.dtos.request.users.SubmitTaskRequest;
import com.itsilesia.si_learn.dtos.response.user_progress.TaskUserSubmittedResponse;
import com.itsilesia.si_learn.dtos.response.user_progress.TaskUserWithContentsResponse;
import com.itsilesia.si_learn.services.user_progress.UserTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
	public TaskUserWithContentsResponse getTaskUser(@PathVariable UUID taskId,
	                                                @Parameter(hidden = true) @AuthenticationPrincipal UUID userId) {
		return TaskUserWithContentsResponse.fromEntity(
			userTaskService.getTaskUser(taskId, userId));
	}

	@PostMapping("{taskId}/submissions")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Operation(summary = "Sends exercise to be checked")
	public TaskUserSubmittedResponse submitTask(@PathVariable UUID taskId,
	                                            @Parameter(hidden = true) @AuthenticationPrincipal UUID userId,
	                                            @Valid @RequestBody SubmitTaskRequest request) {
		return TaskUserSubmittedResponse.fromEntity(
			userTaskService.submitTask(taskId, userId, request)
		);
	}


}