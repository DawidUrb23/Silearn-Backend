package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.user_progress.UserTask;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TaskSubmissionResponse {
	private UUID userId;
	private String userFullName;
	private String submittedCode;

	public TaskSubmissionResponse(@NotNull UserTask userTask) {
		this.userId = userTask.getUser().getId();
		this.userFullName = userTask.getUser().getName() + " " + userTask.getUser().getSurname();
		this.submittedCode = userTask.getUserSolution();
	}

	public static TaskSubmissionResponse fromEntity(@NotNull UserTask userTask) {
		return new TaskSubmissionResponse(userTask);
	}
}
