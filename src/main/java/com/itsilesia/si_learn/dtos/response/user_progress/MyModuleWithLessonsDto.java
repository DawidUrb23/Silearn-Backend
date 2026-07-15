package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.entities.user_progress.UserModule;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.yaml.snakeyaml.util.Tuple;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MyModuleWithLessonsDto extends MyModuleDto {
	private int lessonCount;
	private List<MyLessonDto> lessons;

	public MyModuleWithLessonsDto(@NonNull Module module, UserModule userModule) {
		super(module, userModule);
		var lessons = module.getLessons();
		var userLessons = userModule.getUserLessons();

		//TODO: after merge use the abstracted method
		var userItemsMap = userLessons.stream().collect(
			Collectors.toMap(it -> it.getModule().getId(), item -> item)
		);
		var userJoinedItems = lessons.stream()
			.map(item -> new Tuple<>(item, userItemsMap.get(item.getId())))
			.toList();

		this.lessonCount = lessons.size();
		this.lessons = userJoinedItems.stream().map(MyLessonDto::fromEntity).toList();
	}

	static public MyModuleWithLessonsDto fromEntity(@NonNull UserModule userModule) {
		return new MyModuleWithLessonsDto(userModule.getModule(), userModule);
	}
}
