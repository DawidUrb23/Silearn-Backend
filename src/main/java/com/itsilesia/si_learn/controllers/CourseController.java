package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.courses.CreateCourseRequest;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.services.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
	private final CourseService courseService;

	@PostMapping
	public Course createCourse(@Valid @RequestBody CreateCourseRequest request) {
		return courseService.createCourse(request);
	}
}
