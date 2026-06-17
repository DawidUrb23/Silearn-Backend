package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.courses.CreateCourseRequest;
import com.itsilesia.si_learn.dtos.response.IdOnlyDto;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.services.CourseService;
import com.itsilesia.si_learn.services.UserCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
	private final CourseService courseService;
	private final UserCourseService userCourseService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new course (Teacher)", description = "Creates a new Course, ")
	@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404", description = "Something does not exist",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public IdOnlyDto createCourse(
		@Valid @RequestBody CreateCourseRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		return IdOnlyDto.fromEntity(
			courseService.createCourse(request, userId)
		);
	}

	@PostMapping("/{id}/enroll")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void enrollToCourse(
		@PathVariable UUID id,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		userCourseService.enroll(id, userId);
	}
}
