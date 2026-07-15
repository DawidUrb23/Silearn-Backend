package com.itsilesia.si_learn.controllers.courses;

import com.itsilesia.si_learn.dtos.request.courses.ContentsOrderUpdateRequest;
import com.itsilesia.si_learn.dtos.request.courses.CreateTaskRequest;
import com.itsilesia.si_learn.dtos.response.IdOnlyDto;
import com.itsilesia.si_learn.dtos.response.courses.LessonWithTasksResponse;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.services.CourseHelperService;
import com.itsilesia.si_learn.services.LessonService;
import com.itsilesia.si_learn.services.TaskService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@Validated
@Tag(
	name = "Lessons",
	description = "Endpoints for: managing lessons (Owner Teacher)"
)
public class LessonController {
	private final LessonService lessonService;
	private final TaskService taskService;
	private final CourseHelperService courseHelperService;

	@PostMapping("/{id}/tasks")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<IdOnlyDto> createTask(
		@PathVariable UUID id,
		@RequestBody @Valid CreateTaskRequest createTaskRequest,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		IdOnlyDto dto = IdOnlyDto.fromEntity(
			taskService.createTask(createTaskRequest, id, userId));
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}

	@PutMapping("/{id}/tasks")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Update task order in lesson (Owner)")
	@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void updateTaskOrder(
		@PathVariable UUID id,
		@RequestBody @Valid ContentsOrderUpdateRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		taskService.updateTaskOrder(id, request, userId);
	}

	@GetMapping("/{id}")
	public LessonWithTasksResponse getLessonWithContent(
		@PathVariable UUID id,
		@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String languageCode,  //exposes the header for LocaleContextHolder
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		var lesson = lessonService.getLessonWithContent(id);
		courseHelperService.isAuthorOrThrow(lesson, userId);
		return LessonWithTasksResponse.fromEntity(lesson);
	}

	@DeleteMapping("/{lessonId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete lesson (Teacher)")
	@ApiResponse(responseCode = "404", description = "Lesson not found",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public ResponseEntity<Void> deleteLesson(@PathVariable UUID lessonId) {
		lessonService.deleteLesson(lessonId);
		return ResponseEntity.noContent().build();
	}
}
