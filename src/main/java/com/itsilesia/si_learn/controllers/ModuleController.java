package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.courses.CreateModuleRequest;
import com.itsilesia.si_learn.dtos.response.IdOnlyDto;
import com.itsilesia.si_learn.services.ModuleService;
import com.itsilesia.si_learn.dtos.response.courses.LessonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {
	private final ModuleService moduleService;

	@PostMapping("/courses/{courseId}")
	@ResponseStatus(HttpStatus.CREATED)
	public IdOnlyDto createModule(
		@PathVariable UUID courseId,
		@Valid @RequestBody CreateModuleRequest request
	) {
		return IdOnlyDto.fromEntity(
			moduleService.createModule(courseId, request)
		);
	}
	@GetMapping("/{moduleId}/lessons")
	@ResponseStatus(HttpStatus.OK)
	public List<LessonResponse> getModuleLessons(
		@PathVariable UUID moduleId,
		@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String languageCode
	) {
		return moduleService.getModuleWithLessons(moduleId)
			.getLessons()
			.stream()
			.map(lesson -> LessonResponse.fromEntity(lesson, languageCode))
			.toList();
	}
}
