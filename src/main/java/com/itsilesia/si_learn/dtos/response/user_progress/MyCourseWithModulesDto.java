package com.itsilesia.si_learn.dtos.response.user_progress;


import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import com.itsilesia.si_learn.entities.user_progress.UserModule;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.yaml.snakeyaml.util.Tuple;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MyCourseWithModulesDto extends MyCourseOverviewDto {
	private int moduleCount;
	private List<MyModuleDto> modules;

	public MyCourseWithModulesDto(@NotNull UserCourse userCourse) {
		super(userCourse);
		var modules = userCourse.getCourse().getModules();
		var userModulesMap = userCourse.getUserModules().stream().collect(
			Collectors.toMap(it -> it.getModule().getId(), item -> item)
		);
		List<Tuple<@NonNull Module, UserModule>> userJoinedItems = modules.stream()
			.map(item -> new Tuple<>(item, userModulesMap.get(item.getId())))
			.toList();

		this.moduleCount = modules.size();
		this.modules = userJoinedItems.stream().map(MyModuleDto::fromEntity).toList();
	}

	static public MyCourseWithModulesDto fromEntity(@NotNull UserCourse userCourse) {
		return new MyCourseWithModulesDto(userCourse);
	}
}
