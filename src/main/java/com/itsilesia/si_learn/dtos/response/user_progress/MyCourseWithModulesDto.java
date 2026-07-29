package com.itsilesia.si_learn.dtos.response.user_progress;


import com.itsilesia.si_learn.entities.abstractions.EntityWithOrderedChildren;
import com.itsilesia.si_learn.entities.abstractions.IndexedEntity;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class MyCourseWithModulesDto extends MyCourseOverviewDto {
	private int moduleCount;
	private List<MyModuleDto> modules;

	public MyCourseWithModulesDto(@NotNull UserCourse userCourse) {
		super(userCourse);
		var modules = userCourse.getCourse().getModules();
		modules.sort(Comparator.comparingInt(IndexedEntity::getIndex)); // Sorts ascending by Index;
		var userJoinedItems = EntityWithOrderedChildren.JoinItemWithUserItem(modules, userCourse.getUserModules());

		this.moduleCount = modules.size();
		this.modules = userJoinedItems.stream().map(MyModuleDto::fromEntity).toList();
	}

	static public MyCourseWithModulesDto fromEntity(@NotNull UserCourse userCourse) {
		return new MyCourseWithModulesDto(userCourse);
	}
}
