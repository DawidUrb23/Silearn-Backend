package com.itsilesia.si_learn.dtos.response.user_progress;


import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyCourseWithModulesDto {
	private UUID courseId;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private ProgressStatus progressStatus;
	private int moduleCount;

	private List<MyModuleDetailsDto> modules;

	static public MyCourseWithModulesDto fromEntity(UserCourse course, String languageCode) {
		var modules = course.getCourse().getModules();
		return new MyCourseWithModulesDto(
			course.getCourse().getId(),
			course.getStartDate(),
			course.getEndDate(),
			course.progressStatus(),
			modules.size(),
			course.getUserModules().stream().map(userModule -> MyModuleDetailsDto.fromEntity(userModule, languageCode)).toList()
		);
	}
}
