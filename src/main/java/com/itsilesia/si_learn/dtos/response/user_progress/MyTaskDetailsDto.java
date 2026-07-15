package com.itsilesia.si_learn.dtos.response.user_progress;
import com.itsilesia.si_learn.dtos.response.courses.TaskResponse;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import com.itsilesia.si_learn.entities.courses.Task;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.NonNull;
import org.yaml.snakeyaml.util.Tuple;


@Getter
@Setter
public class MyTaskDetailsDto {

	private TaskResponse task;
	private float taskScore;
	private boolean passed;
	private ProgressStatus progressStatus;

	public MyTaskDetailsDto(
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

	public static MyTaskDetailsDto fromEntity(
		@NonNull Task task,
		UserTask userTask
	) {
		return new MyTaskDetailsDto(task, userTask);
	}

	public static MyTaskDetailsDto fromEntity(
		@NonNull UserTask userTask
	) {
		return new MyTaskDetailsDto(
			userTask.getTask(),
			userTask
		);
	}

	public static MyTaskDetailsDto fromEntity(
		Tuple<@NonNull Task, UserTask> taskAndProgress
	) {
		return new MyTaskDetailsDto(
			taskAndProgress._1(),
			taskAndProgress._2()
		);
	}

}