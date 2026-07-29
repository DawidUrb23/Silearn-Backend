package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.dtos.response.courses.TaskWithContentResponse;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class TaskUserWithContentsResponse {
	private TaskWithContentResponse task;

	private ProgressStatus progressStatus = ProgressStatus.NOT_STARTED;
	private Float score;
	private boolean passed;
	private String userCode;

	public TaskUserWithContentsResponse(@NotNull Task task, UserTask userTask) {
		this.task = TaskWithContentResponse.fromEntity(task);
		if (userTask == null) return;   //there is no user progress for this task
		this.progressStatus = userTask.progressStatus();
		this.score = userTask.getTaskScore();
		this.passed = userTask.isPassed();
		this.userCode = userTask.getUserSolution();
	}

	public static TaskUserWithContentsResponse fromEntity(@NotNull Task task, UserTask userTask) {
		return new TaskUserWithContentsResponse(task, userTask);
	}

	public static TaskUserWithContentsResponse fromEntity(@NotNull UserTask userTask) {
		return new TaskUserWithContentsResponse(userTask.getTask(), userTask);
	}
}
