package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MyCoursesDto {
	List<MyCourseOverviewDto> courses;
	int enrolled;
	int finished;

	static public MyCoursesDto fromEntities(List<UserCourse> courses) {
		var myCoursesDto = courses.stream().map(MyCourseOverviewDto::fromEntity).toList();
		return new MyCoursesDto(
			myCoursesDto,
			courses.size(),
			(int) myCoursesDto.stream()
				.filter(c -> c.getUserProgress().getProgressStatus() == ProgressStatus.FINISHED)
				.count()
		);
	}
}
