package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.dtos.response.courses.CourseResponse;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.util.Tuple;

@Getter
@Setter
public class MyCourseOverviewDto {
	private CourseResponse course;
	private UserProgressDto userProgress;
	private boolean enrolled;

	public MyCourseOverviewDto(@NotNull UserCourse userCourse) {
		this(userCourse.getCourse(), userCourse);
	}

	public MyCourseOverviewDto(@NotNull Course course, UserCourse userCourse) {
		this.course = CourseResponse.fromEntity(course);
		this.enrolled = userCourse != null;
		if (userCourse != null)
			this.userProgress = UserProgressDto.fromEntity(userCourse);
	}

	static public MyCourseOverviewDto fromEntity(@NotNull UserCourse userCourse) {
		return new MyCourseOverviewDto(userCourse);
	}

	static public MyCourseOverviewDto fromEntity(@NotNull Course course, UserCourse userCourse) {
		return new MyCourseOverviewDto(course, userCourse);
	}

	static public MyCourseOverviewDto fromEntity(@NotNull Tuple<@NotNull Course, UserCourse> tuple) {
		return new MyCourseOverviewDto(tuple._1(), tuple._2());
	}
}
