package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CourseWithModulesResponse extends CourseResponse {
	private List<ModuleResponse> modules;

	public CourseWithModulesResponse(@NotNull Course course, UserCourse userCourse) {
		super(course, userCourse);
		this.modules = course.getModules() != null ? course.getModules().stream().map(ModuleResponse::fromEntity).toList() : List.of();
	}

	public static CourseWithModulesResponse fromEntity(@NotNull Course course, UserCourse userCourse) {
		return new CourseWithModulesResponse(course, userCourse);
	}
}
