package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.courses.Course;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseStatisticsDto {
	private CourseResponse course;
	private long registered;
	private long completed;
	private long inProgress;

	public CourseStatisticsDto(Course course, long registered, long completed, long inProgress) {
		this.course = CourseResponse.fromEntity(course, null);
		this.registered = registered;
		this.completed = completed;
		this.inProgress = inProgress;
	}
}
