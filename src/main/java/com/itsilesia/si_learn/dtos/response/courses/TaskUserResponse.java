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
public class TaskUserResponse {
	private UUID id;
	private Integer index;
	private Float passingScore;
	private String startingCode;
	private UUID lessonId;
	private ProgressStatus progressStatus;
	private Float requiredScore;
	private List<TaskContentDto> taskContents;

	public static TaskUserResponse fromEntity(Task task, UserTask userTask) {
		if (task == null)
			return null;

		List<TaskContentDto> mappedContents = Optional.ofNullable(task.getTaskContents()).orElse(Collections.emptyList()).stream()
			.sorted(Comparator.comparingInt(TaskContent::getIndex)) // Sorts ascending by Index
			.map(TaskContentDto::fromEntity)
			.toList();

		Float userScore = null;
		ProgressStatus status = ProgressStatus.NOT_STARTED;

		if(userTask != null){
			userScore = userTask.getTaskScore();
			status = userTask.progressStatus();
		}

		return TaskUserResponse.builder()
			.id(task.getId())
			.index(task.getIndex())
			.startingCode(task.getStartingCode())
			.passingScore(task.getPassingScore())
			.requiredScore(userScore)
			.progressStatus(status)
			.lessonId(task.getLesson() != null ? task.getLesson().getId() : null)
			.taskContents(mappedContents)
			.build();
	}
}