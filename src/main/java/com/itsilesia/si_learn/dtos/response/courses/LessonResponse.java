package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.DictionaryItem;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.entities.courses.Lesson;

import java.util.UUID;

public record LessonResponse(
	UUID id,
	String topic,
	String description
) {
	public static LessonResponse fromEntity(Lesson lesson) {
		return new LessonResponse(
			lesson.getId(),
			getOriginalValue(lesson.getTopic()),
			getOriginalValue(lesson.getDescription())
		);
	}

	private static String getOriginalValue(TranslationDictionary dictionary) {
		return dictionary.getItems().stream()
			.filter(item -> item.getLanguage().getCode().equals(dictionary.getOriginalLanguage().getCode()))
			.findFirst()
			.map(DictionaryItem::getValue)
			.orElse(null);
	}
}
