package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.dtos.response.user_progress.MyCourseOverviewDto;
import com.itsilesia.si_learn.entities.abstractions.IndexedEntity;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class CourseWithModulesResponse extends MyCourseOverviewDto {
	private List<ModuleResponse> modules;

	public CourseWithModulesResponse(@NotNull Course course, UserCourse userCourse) {
		super(course, userCourse);
		this.modules = course.getModules() != null

			? course.getModules().stream()
			.sorted(Comparator.comparingInt(IndexedEntity::getIndex)) // Sorts ascending by Index
			.map(ModuleResponse::fromEntity).toList()
			: List.of();
	}

	public static CourseWithModulesResponse fromEntity(@NotNull Course course, UserCourse userCourse) {
		return new CourseWithModulesResponse(course, userCourse);
	}
}
