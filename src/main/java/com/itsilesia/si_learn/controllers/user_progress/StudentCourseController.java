package com.itsilesia.si_learn.controllers.user_progress;


import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.dtos.response.user_progress.MyCourseWithModulesDto;
import com.itsilesia.si_learn.dtos.response.user_progress.MyCoursesDto;
import com.itsilesia.si_learn.services.user_progress.UserCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/my/courses")
@RequiredArgsConstructor
@Validated
@Tag(name = "My courses", description = "Endpoints for interacting with enrolled courses (User)")
public class StudentCourseController {
	private final UserCourseService userCourseService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get courses that the user is enrolled in")
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public MyCoursesDto myCourses(
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId,
		@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String languageCode   //exposes the header for LocaleContextHolder
	) {
		return MyCoursesDto.fromEntities(
			userCourseService.getUserCourses(userId)
		);
	}

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get the course with user progress")
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public MyCourseWithModulesDto myCourse(
		@PathVariable UUID id,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId,
		@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String languageCode   //exposes the header for LocaleContextHolder
	) {
		return MyCourseWithModulesDto.fromEntity(
			userCourseService.getUserCourse(userId, id)
		);
	}

}
