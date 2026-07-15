package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskUserSubmittedResponse {
	private ProgressStatus progressStatus;
	private Float score;
	private boolean passed;


	public TaskUserSubmittedResponse(@NotNull UserTask userTask) {
		this.progressStatus = userTask.progressStatus();
		this.score = userTask.getTaskScore();
		this.passed = userTask.isPassed();
	}

	public static TaskUserSubmittedResponse fromEntity(@NotNull UserTask userTask) {
		return new TaskUserSubmittedResponse(userTask);
	}
}
