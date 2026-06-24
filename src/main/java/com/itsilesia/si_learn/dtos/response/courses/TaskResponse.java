package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDto;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.courses.TaskContent;
import lombok.*;

import java.util.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TaskResponse {
	private UUID id;
	private Integer index;
	private Float passingScore;
	private String startingCode;
	private UUID lessonId;
	private List<TaskContentDto> taskContent;

	public static TaskResponse taskDtoMapper(Task task) {
		if (task == null)
			return null;

		List<TaskContentDto> mappedContents = Optional.ofNullable(task.getTaskContents()).orElse(Collections.emptyList()).stream()
			.sorted(Comparator.comparingInt(TaskContent::getIndex)) // Sorts ascending by Index
			.map(content -> {
				//Fixme: use translation dictionary
				TranslationDto dictionaryContent = null;
				if (content.getContent() != null && content.getContent().getItems() != null && !content.getContent().getItems().isEmpty()) {
					dictionaryContent = TranslationDto.fromEntity(
						content.getContent().getItems().get(0)
					);
				}
				return TaskContentDto.builder()
					.id(content.getId())
					.index(content.getIndex())
					.type(content.getType())
					.content(dictionaryContent)
					.build();
			})
			.toList();

		return TaskResponse.builder()
			.id(task.getId())
			.index(task.getIndex())
			.startingCode(task.getStartingCode())
			.passingScore(task.getPassingScore())
			.lessonId(task.getLesson() != null ? task.getLesson().getId() : null)
			.taskContent(mappedContents)
			.build();
	}
}