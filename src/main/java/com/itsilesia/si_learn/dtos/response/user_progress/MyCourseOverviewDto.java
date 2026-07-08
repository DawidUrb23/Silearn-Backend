package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.dtos.response.courses.CourseResponse;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyCourseOverviewDto {
	private CourseResponse course;
	private UserProgressDto userProgress;

	public MyCourseOverviewDto(@NotNull UserCourse userCourse) {
		this.course = CourseResponse.fromEntity(userCourse.getCourse(), userCourse);
		this.userProgress = UserProgressDto.fromEntity(userCourse);
	}

	static public MyCourseOverviewDto fromEntity(@NotNull UserCourse userCourse) {
		return new MyCourseOverviewDto(userCourse);
	}
}
