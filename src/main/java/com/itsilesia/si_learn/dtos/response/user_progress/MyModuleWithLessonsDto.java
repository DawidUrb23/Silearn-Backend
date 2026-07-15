package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.entities.abstractions.EntityWithOrderedChildren;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.entities.user_progress.UserModule;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyModuleWithLessonsDto extends MyModuleDto {
	private int lessonCount;
	private List<MyLessonDto> lessons;

	public MyModuleWithLessonsDto(@NonNull Module module, UserModule userModule) {
		super(module, userModule);
		var lessons = module.getLessons();
		var userLessons = userModule.getUserLessons();

		var tuples = EntityWithOrderedChildren.JoinItemWithUserItem(lessons, userLessons);
		this.lessonCount = lessons.size();
		this.lessons = tuples.stream().map(MyLessonDto::fromEntity).toList();
	}

	static public MyModuleWithLessonsDto fromEntity(@NonNull UserModule userModule) {
		return new MyModuleWithLessonsDto(userModule.getModule(), userModule);
	}
}
