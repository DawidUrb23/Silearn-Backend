package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.courses.CreateTaskRequest;
import com.itsilesia.si_learn.dtos.response.IdOnlyDto;
import com.itsilesia.si_learn.dtos.response.courses.LessonWithContentResponse;
import com.itsilesia.si_learn.services.CourseHelperService;
import com.itsilesia.si_learn.services.LessonService;
import com.itsilesia.si_learn.services.TaskService;
import io.swagger.v3.oas.annotations.Parameter;
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

	@GetMapping("/{id}")
	public LessonWithContentResponse getLessonWithContent(
		@PathVariable UUID id,
		@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String languageCode,  //exposes the header for LocaleContextHolder
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		var lesson = lessonService.getLessonWithContent(id);
		courseHelperService.isAuthorOrThrow(lesson, userId);
		return LessonWithContentResponse.fromEntity(lesson);
	}
}
