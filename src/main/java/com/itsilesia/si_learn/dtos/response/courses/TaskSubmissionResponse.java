package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.user_progress.UserTask;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TaskSubmissionResponse(
	UUID userId,
	String userFullName,
	String submittedCode
) {
	public static TaskSubmissionResponse fromEntity(@NotNull UserTask userTask) {
		return new TaskSubmissionResponse(
			userTask.getUser().getId(),
			userTask.getUser().getName() + " " + userTask.getUser().getSurname(),
			userTask.getUserSolution()
		);
	}
}
