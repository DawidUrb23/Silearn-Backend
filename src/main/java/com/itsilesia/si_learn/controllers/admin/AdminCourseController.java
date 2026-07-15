package com.itsilesia.si_learn.controllers.admin;

import com.itsilesia.si_learn.dtos.request.courses.ChangeCourseStatusRequest;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
@Validated
@Tag(name = "Admin Courses", description = "Admin endpoints for managing courses")
public class AdminCourseController {
	private final CourseService courseService;

	@PatchMapping("/{courseId}/status")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Change course status (Admin)")
	@ApiResponse(responseCode = "400", description = "Invalid request body",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404", description = "Course not found",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void changeCourseStatus(
		@PathVariable UUID courseId,
		@Valid @RequestBody ChangeCourseStatusRequest request
	) {
		courseService.changeStatus(courseId, request.status());
	}

	@DeleteMapping("{courseId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Change course status DELETED (Admin)")
	@ApiResponse(responseCode = "404", description = "Course not found",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void changeCourseToDeleted(@PathVariable UUID courseId){
		courseService.setStatusDeleted(courseId);
	}
}
