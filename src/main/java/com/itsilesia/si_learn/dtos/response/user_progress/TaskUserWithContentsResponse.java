package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.dtos.response.courses.TaskContentDto;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TaskUserWithContentsResponse extends TaskUserResponse {
	private List<TaskContentDto> taskContents;

	public TaskUserWithContentsResponse(@NotNull Task task, UserTask userTask) {
		super(task, userTask);
		taskContents = task.getTaskContents().stream().map(TaskContentDto::fromEntity).toList();
	}
}
