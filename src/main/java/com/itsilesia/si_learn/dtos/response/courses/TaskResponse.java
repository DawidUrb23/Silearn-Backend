package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.courses.Task;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TaskResponse {
	private UUID id;
	private Float passingScore;
	private String startingCode;

	public TaskResponse(Task task) {
		this.id = task.getId();
		this.startingCode = task.getStartingCode();
		this.passingScore = task.getPassingScore();
	}

	public static TaskResponse fromEntity(Task task) {
		return task == null ? null : new TaskResponse(task);
	}
}