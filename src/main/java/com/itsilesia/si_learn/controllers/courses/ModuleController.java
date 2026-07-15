package com.itsilesia.si_learn.controllers.courses;

import com.itsilesia.si_learn.dtos.request.courses.ContentsOrderUpdateRequest;
import com.itsilesia.si_learn.dtos.request.courses.CreateLessonRequest;
import com.itsilesia.si_learn.dtos.response.IdOnlyDto;
import com.itsilesia.si_learn.dtos.response.courses.ModuleWithLessonsResponse;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.services.CourseHelperService;
import com.itsilesia.si_learn.services.LessonService;
import com.itsilesia.si_learn.services.ModuleService;
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
import org.springframework.web.bind.annotation.*;

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

	@PutMapping("/{moduleId}/lessons")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Update lesson order in module (Owner)")
	@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void updateLessonOrder(
		@PathVariable UUID moduleId,
		@Valid @RequestBody ContentsOrderUpdateRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		lessonService.updateLessonOrder(moduleId, request, userId);
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
