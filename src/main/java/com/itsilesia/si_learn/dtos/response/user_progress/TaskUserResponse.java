package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.dtos.response.courses.TaskResponse;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class TaskUserResponse {
	private TaskResponse task;

	private ProgressStatus progressStatus = ProgressStatus.NOT_STARTED;
	private Float score;
	private boolean passed;

	public TaskUserResponse(@NotNull Task task, UserTask userTask) {
		this.task = TaskResponse.fromEntity(task);
		if (userTask == null) return;   //there is no user progress for this task
		this.progressStatus = userTask.progressStatus();
		this.score = userTask.getTaskScore();
		this.passed = userTask.isPassed();
	}

	public static TaskUserResponse fromEntity(@NotNull Task task, UserTask userTask) {
		return new TaskUserResponse(task, userTask);
	}
}
