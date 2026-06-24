package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.courses.CreateCourseRequest;
import com.itsilesia.si_learn.dtos.request.courses.CreateModuleRequest;
import com.itsilesia.si_learn.dtos.response.IdOnlyDto;
import com.itsilesia.si_learn.dtos.response.courses.CourseResponse;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import com.itsilesia.si_learn.services.CourseService;
import com.itsilesia.si_learn.services.ModuleService;
import com.itsilesia.si_learn.services.UserCourseService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Validated
@Tag(name = "Courses", description = "Endpoints for: managing courses (Teacher), getting course info (Public)")
public class CourseController {
	private final CourseService courseService;
	private final UserCourseService userCourseService;
	private final ModuleService moduleService;


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

	@PostMapping("/{courseId}/enroll")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Enroll into the course (Student)")
	@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void enrollToCourse(
		@PathVariable UUID courseId,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		userCourseService.enroll(courseId, userId);
	}


	@PostMapping("/{courseId}/modules")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new module for course (Teacher)")
	@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public IdOnlyDto createModule(
		@PathVariable UUID courseId,
		@Valid @RequestBody CreateModuleRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		return IdOnlyDto.fromEntity(
			moduleService.createModule(courseId, request)
		);
	}

	@GetMapping("/{courseId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get specific course details with enrolment status")
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public CourseResponse getCourse(
		@PathVariable UUID courseId,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId,
		@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String languageCode
	) {
		Course course = courseService.getCourse(courseId);
		UserCourse userCourse = null;
		try {
			//we use try catch to not crash the flow when user is not registered into the course
			userCourse = userCourseService.getUserCourse(userId, courseId);
		} catch (Exception _) {
		}
		return CourseResponse.fromEntity(course, languageCode, userCourse);
	}

//	@GetMapping("/{courseId}/modules") //gets a list of modules in course (Teacher)

}
