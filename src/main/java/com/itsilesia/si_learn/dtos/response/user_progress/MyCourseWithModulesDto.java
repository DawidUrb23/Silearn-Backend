package com.itsilesia.si_learn.dtos.response.user_progress;


import com.itsilesia.si_learn.dtos.response.courses.ModuleResponse;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyCourseWithModulesDto extends MyCourseOverviewDto {
	private int moduleCount;
	private List<ModuleResponse> modules;

	public MyCourseWithModulesDto(@NotNull UserCourse userCourse) {
		super(userCourse);
		var modules = userCourse.getCourse().getModules();
		this.moduleCount = modules.size();
		this.modules = modules.stream().map(ModuleResponse::fromEntity).toList();
	}

	static public MyCourseWithModulesDto fromEntity(@NotNull UserCourse userCourse) {
		return new MyCourseWithModulesDto(userCourse);
	}
}
