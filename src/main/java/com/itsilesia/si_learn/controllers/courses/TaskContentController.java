package com.itsilesia.si_learn.controllers.courses;

import com.itsilesia.si_learn.dtos.request.courses.TaskContentRequest;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.services.TaskContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/task-contents")
@Validated
@Tag(name = "Task Contents", description = "manage TaskContents (Owner Teacher)")
public class TaskContentController {
	private final TaskContentService taskContentService;

	@PatchMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Update TaskContent (Owner)")
	@ApiResponse(responseCode = "403", description = "Not the owner!",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void updateTaskContent(
		@PathVariable UUID id,
		@RequestBody @Valid TaskContentRequest taskRequest,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		taskContentService.updateContent(id, taskRequest, userId);
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete TaskContent (Owner)")
	@ApiResponse(responseCode = "403", description = "Not the owner!",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void deleteTaskContent(
		@PathVariable UUID id,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		taskContentService.delete(id, userId);
	}
}
