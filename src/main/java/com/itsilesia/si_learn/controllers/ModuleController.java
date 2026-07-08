package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.courses.CreateLessonRequest;
import com.itsilesia.si_learn.dtos.response.IdOnlyDto;
import com.itsilesia.si_learn.dtos.response.courses.LessonResponse;
import com.itsilesia.si_learn.dtos.response.courses.ModuleResponse;
import com.itsilesia.si_learn.dtos.response.courses.ModuleWithLessonsResponse;
import com.itsilesia.si_learn.services.CourseHelperService;
import com.itsilesia.si_learn.services.LessonService;
import com.itsilesia.si_learn.services.ModuleService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
@Tag(
	name = "Modules",
	description = "Endpoints for: managing modules and module lessons (Owner Teacher)"
)
public class ModuleController {
	private final ModuleService moduleService;
	private final LessonService lessonService;
	private final CourseHelperService courseHelperService;

	@PostMapping("/{moduleId}/lessons")
	@ResponseStatus(HttpStatus.CREATED)
	public IdOnlyDto createLesson(
		@PathVariable UUID moduleId,
		@Valid @RequestBody CreateLessonRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		return IdOnlyDto.fromEntity(
			lessonService.createLesson(moduleId, request, userId)
		);
	}

	@GetMapping("/{moduleId}")
	@ResponseStatus(HttpStatus.OK)
	public ModuleWithLessonsResponse getModule(
		@PathVariable UUID moduleId,
		@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String languageCode,  //exposes the header for LocaleContextHolder
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		var module = moduleService.getModuleWithLessons(moduleId);
		courseHelperService.isAuthorOrThrow(module, userId);
		return ModuleWithLessonsResponse.fromEntity(module);
	}

}
