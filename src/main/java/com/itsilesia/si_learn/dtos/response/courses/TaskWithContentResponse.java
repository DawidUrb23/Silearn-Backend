package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.abstractions.IndexedEntity;
import com.itsilesia.si_learn.entities.courses.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class TaskWithContentResponse extends TaskResponse {
	private List<TaskContentDto> taskContents;

	public TaskWithContentResponse(Task task) {
		super(task);
		this.taskContents = Optional.ofNullable(task.getTaskContents()).orElse(Collections.emptyList()).stream()
			.sorted(Comparator.comparingInt(IndexedEntity::getIndex)) // Sorts ascending by Index
			.map(TaskContentDto::fromEntity)
			.toList();
	}

	public static TaskWithContentResponse fromEntity(Task task) {
		return task == null ? null : new TaskWithContentResponse(task);
	}
}
