package com.itsilesia.si_learn.controllers.user_progress;


import com.itsilesia.si_learn.dtos.response.user_progress.MyCourseWithModulesDto;
import com.itsilesia.si_learn.dtos.response.user_progress.MyCoursesDto;
import com.itsilesia.si_learn.services.UserCourseService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/my/courses")
@RequiredArgsConstructor
@Validated
@Tag(name = "My courses", description = "Endpoints for interacting with enrolled courses")
public class StudentCourseController {
	private final UserCourseService userCourseService;

	@GetMapping
	public MyCoursesDto myCourses(
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		return MyCoursesDto.fromEntities(
			userCourseService.getUserCourses(userId)
		);
	}

	@GetMapping("{id}")
	public MyCourseWithModulesDto myCourse(
		@PathVariable UUID id,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		return MyCourseWithModulesDto.fromEntity(
			userCourseService.getUserCourse(userId, id)
		);
	}

}
