package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.courses.CreateLessonRequest;
import com.itsilesia.si_learn.dtos.response.IdOnlyDto;
import com.itsilesia.si_learn.services.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {
	private final LessonService lessonService;

	@PostMapping("/modules/{moduleId}")
	@ResponseStatus(HttpStatus.CREATED)
	public IdOnlyDto createLesson(
		@PathVariable UUID moduleId,
		@Valid @RequestBody CreateLessonRequest request
	) {
		return IdOnlyDto.fromEntity(
			lessonService.createLesson(moduleId, request)
		);
	}
}
