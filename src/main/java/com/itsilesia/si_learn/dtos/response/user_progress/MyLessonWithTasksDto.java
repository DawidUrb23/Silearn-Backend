package com.itsilesia.si_learn.dtos.response.user_progress;
import com.itsilesia.si_learn.entities.abstractions.EntityWithOrderedChildren;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.user_progress.UserLesson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MyLessonWithTasksDto extends MyLessonDto{
	private int taskCount;
	private List<MyTaskDetailsDto> tasks;

	public MyLessonWithTasksDto(
		@NonNull Lesson lesson,
		UserLesson userLesson
	) {
		super(lesson, userLesson);

		var tasks = lesson.getTasks();
		var userTasks = userLesson.getUserTasks();

		var tuples = EntityWithOrderedChildren.JoinItemWithUserItem(
			tasks,
			userTasks
		);

		this.taskCount = tasks.size();
		this.tasks = tuples.stream()
			.map(MyTaskDetailsDto::fromEntity)
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
