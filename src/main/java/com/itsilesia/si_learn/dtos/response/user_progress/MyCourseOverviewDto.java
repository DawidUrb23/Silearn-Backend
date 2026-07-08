package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.dtos.response.TwoIntsProgress;
import com.itsilesia.si_learn.dtos.response.courses.CourseResponse;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyCourseOverviewDto extends CourseResponse {
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private ProgressStatus progressStatus;
	private TwoIntsProgress progress;

	public MyCourseOverviewDto(@NotNull UserCourse userCourse) {
		super(userCourse.getCourse(), userCourse);

		this.startDate = userCourse.getStartDate();
		this.endDate = userCourse.getEndDate();
		this.progressStatus = userCourse.progressStatus();
		this.progress = userCourse.progress();
	}

	static public MyCourseOverviewDto fromEntity(@NotNull UserCourse userCourse) {
		return new MyCourseOverviewDto(userCourse);
	}
}
