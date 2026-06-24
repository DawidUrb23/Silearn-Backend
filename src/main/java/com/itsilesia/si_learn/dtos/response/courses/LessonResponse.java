package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDto;
import com.itsilesia.si_learn.dtos.response.helper.DictionaryHelper;
import com.itsilesia.si_learn.entities.courses.Lesson;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LessonResponse(
	UUID id,
	TranslationDto topic,
	TranslationDto description
) {
	public static LessonResponse fromEntity(
		@NotNull Lesson lesson,
		String languageCode
	) {
		return new LessonResponse(
			lesson.getId(),
			TranslationDto.fromEntity(
				DictionaryHelper.getTranslationWithCode(lesson.getTopic(), languageCode)
			),
			TranslationDto.fromEntity(
				DictionaryHelper.getTranslationWithCode(lesson.getDescription(), languageCode)
			)
		);
	}
}
