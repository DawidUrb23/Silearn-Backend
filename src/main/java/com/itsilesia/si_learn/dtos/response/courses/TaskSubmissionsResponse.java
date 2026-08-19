package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class TaskSubmissionsResponse extends TaskResponse {
	private List<TaskSubmissionResponse> submissions;

	public TaskSubmissionsResponse(@NotNull Task task, List<@NotNull UserTask> userTasks) {
		super(task);
		this.submissions = userTasks.stream().map(TaskSubmissionResponse::fromEntity).toList();
	}

	public static TaskSubmissionsResponse fromEntity(@NotNull Task task, List<@NotNull UserTask> userTasks) {
		return new TaskSubmissionsResponse(task, userTasks);
	}
}
