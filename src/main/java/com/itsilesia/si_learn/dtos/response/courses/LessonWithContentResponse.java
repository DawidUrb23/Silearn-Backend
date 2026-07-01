package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDto;
import com.itsilesia.si_learn.dtos.response.helper.DictionaryHelper;
import com.itsilesia.si_learn.entities.courses.Lesson;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record LessonWithContentResponse(
	UUID id,
	TranslationDto topic,
	TranslationDto description,
	List<TaskResponse> tasks
) {
	public static LessonWithContentResponse fromEntity(@NotNull Lesson lesson) {
		return new LessonWithContentResponse(
			lesson.getId(),
			TranslationDto.fromEntity(
				DictionaryHelper.getTranslationWithCode(lesson.getTopic())
			),
			TranslationDto.fromEntity(
				DictionaryHelper.getTranslationWithCode(lesson.getDescription())
			),
			lesson.getTasks().stream().map(TaskResponse::fromEntity).toList()
		);
	}
}
