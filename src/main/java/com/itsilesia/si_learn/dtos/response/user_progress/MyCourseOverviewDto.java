package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.dtos.response.TwoIntsProgress;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyCourseOverviewDto {
	private UUID courseId;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private ProgressStatus progressStatus;
	private TwoIntsProgress progress;

	static public MyCourseOverviewDto fromEntity(UserCourse course) {
		return new MyCourseOverviewDto(
			course.getCourse().getId(),
			course.getStartDate(),
			course.getEndDate(),
			course.progressStatus(),
			course.progress()
		);
	}
}
