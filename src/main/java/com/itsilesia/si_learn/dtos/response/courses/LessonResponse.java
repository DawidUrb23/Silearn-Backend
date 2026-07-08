package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDto;
import com.itsilesia.si_learn.dtos.response.helper.DictionaryHelper;
import com.itsilesia.si_learn.entities.courses.Lesson;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class LessonResponse {
	private UUID id;
	private TranslationDto topic;
	private TranslationDto description;

	public LessonResponse(@NotNull Lesson lesson) {
		this.id = lesson.getId();
		this.topic = TranslationDto.fromEntity(
			DictionaryHelper.getTranslationWithCode(lesson.getTopic())
		);
		this.description = TranslationDto.fromEntity(
			DictionaryHelper.getTranslationWithCode(lesson.getDescription())
		);
	}

	public static LessonResponse fromEntity(@NotNull Lesson lesson) {
		return new LessonResponse(lesson);
	}
}
