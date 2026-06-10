package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.DictionaryItem;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.entities.courses.Course;

import java.util.UUID;

public record CourseResponse(
	UUID id,
	String title,
	String description
) {
	public static CourseResponse fromEntity(Course course) {
		return new CourseResponse(
			course.getId(),
			getOriginalValue(course.getTitle()),
			getOriginalValue(course.getDescription())
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
