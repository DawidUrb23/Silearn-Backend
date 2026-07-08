package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.courses.TaskContent;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import lombok.*;

import java.util.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TaskResponse {
	private UUID id;
	private Integer index;
	private Float passingScore;
	private String startingCode;
	private UUID lessonId;
	private List<TaskContentDto> taskContents;

	public static TaskResponse fromEntity(Task task) {
		if (task == null)
			return null;

		List<TaskContentDto> mappedContents = Optional.ofNullable(task.getTaskContents()).orElse(Collections.emptyList()).stream()
			.sorted(Comparator.comparingInt(TaskContent::getIndex)) // Sorts ascending by Index
			.map(TaskContentDto::fromEntity)
			.toList();


		return TaskResponse.builder()
			.id(task.getId())
			.index(task.getIndex())
			.startingCode(task.getStartingCode())
			.passingScore(task.getPassingScore())
			.lessonId(task.getLesson() != null ? task.getLesson().getId() : null)
			.taskContents(mappedContents)
			.build();
	}
}