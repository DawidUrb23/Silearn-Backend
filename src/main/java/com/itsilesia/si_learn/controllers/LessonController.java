package com.itsilesia.si_learn.controllers;
import com.itsilesia.si_learn.dtos.request.courses.CreateLessonRequest;
import com.itsilesia.si_learn.dtos.response.courses.LessonResponse;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.services.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {
	private final LessonService lessonService;

	@PostMapping("/modules/{moduleId}")
	public LessonResponse createLesson(
		@PathVariable UUID moduleId,
		@Valid @RequestBody CreateLessonRequest request
	) {
		Lesson lesson = lessonService.createLesson(moduleId, request);
		return LessonResponse.fromEntity(lesson);
	}
}
