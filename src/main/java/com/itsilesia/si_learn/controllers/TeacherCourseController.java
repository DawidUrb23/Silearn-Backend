package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.response.courses.CourseStatisticsDto;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.services.user_progress.TeacherCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teacher/courses")
@RequiredArgsConstructor
@Validated
@Tag(name = "Teacher Courses", description = "Endpoints for managing authored courses and viewing statistics (Teacher)")
public class TeacherCourseController {

	private final TeacherCourseService teacherCourseService;

	@GetMapping("/statistics")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get statistics for all authored courses")
	public List<CourseStatisticsDto> getCoursesStatistics(
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID teacherId
	) {
		return teacherCourseService.getCoursesStatistics(teacherId, fromDate, toDate);
	}

	@GetMapping("/statistics/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get statistics for a single authored course")
	@ApiResponse(responseCode = "404", description = "Course not found",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public CourseStatisticsDto getCourseStatistics(
		@PathVariable UUID id,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID teacherId
	) {
		return teacherCourseService.getCourseStatisticsById(id, teacherId, fromDate, toDate);
	}
}
