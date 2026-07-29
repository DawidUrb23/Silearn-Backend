package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.entities.abstractions.EntityWithOrderedChildren;
import com.itsilesia.si_learn.entities.abstractions.IndexedEntity;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.user_progress.UserLesson;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class MyLessonWithTasksDto extends MyLessonDto {
	private int taskCount;
	private List<MyTaskDto> tasks;

	public MyLessonWithTasksDto(
		@NonNull Lesson lesson,
		UserLesson userLesson
	) {
		super(lesson, userLesson);

		var tasks = lesson.getTasks();
		tasks.sort(Comparator.comparingInt(IndexedEntity::getIndex)); // Sorts ascending by Index;
		var userTasks = userLesson.getUserTasks();

		var tuples = EntityWithOrderedChildren.JoinItemWithUserItem(
			tasks,
			userTasks
		);

		this.taskCount = tasks.size();
		this.tasks = tuples.stream()
			.map(MyTaskDto::fromEntity)
			.toList();
	}

	public static MyLessonWithTasksDto fromEntity(
		@NonNull UserLesson userLesson
	) {
		return new MyLessonWithTasksDto(
			userLesson.getLesson(),
			userLesson
		);
	}
}
