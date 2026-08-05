package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.dtos.response.courses.TaskResponse;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.yaml.snakeyaml.util.Tuple;


@Getter
@Setter
public class MyTaskDto {
	private TaskResponse task;
	private Float taskScore;
	private boolean passed;
	private ProgressStatus progressStatus;

	public MyTaskDto(
		@NotNull Task task,
		UserTask userTask
	) {
		this.task = TaskResponse.fromEntity(task);

		if (userTask != null) {
			this.taskScore = userTask.getTaskScore();
			this.passed = userTask.isPassed();
			this.progressStatus = userTask.progressStatus();
		}
	}

	public static MyTaskDto fromEntity(
		@NonNull Task task,
		UserTask userTask
	) {
		return new MyTaskDto(task, userTask);
	}

	public static MyTaskDto fromEntity(
		@NonNull UserTask userTask
	) {
		return new MyTaskDto(
			userTask.getTask(),
			userTask
		);
	}

	public static MyTaskDto fromEntity(
		Tuple<@NonNull Task, UserTask> taskAndProgress
	) {
		return new MyTaskDto(
			taskAndProgress._1(),
			taskAndProgress._2()
		);
	}

}