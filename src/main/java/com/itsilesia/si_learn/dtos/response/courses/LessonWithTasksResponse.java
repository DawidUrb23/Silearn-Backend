package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.courses.Lesson;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LessonWithTasksResponse extends LessonResponse {
	private List<TaskResponse> tasks;

	public LessonWithTasksResponse(@NotNull Lesson lesson) {
		super(lesson);
		this.tasks = lesson.getTasks() != null ? lesson.getTasks().stream().map(TaskResponse::fromEntity).toList() : List.of();
	}

	public static LessonWithTasksResponse fromEntity(@NotNull Lesson lesson) {
		return new LessonWithTasksResponse(lesson);
	}
}
