package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.dtos.response.dictonary.LanguageDto;
import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDto;
import com.itsilesia.si_learn.dtos.response.helper.DictionaryHelper;
import com.itsilesia.si_learn.dtos.response.users.UserGeneralDataDto;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.enums.DifficultyLevel;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.entities.enums.VisibilityStatus;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseResponse(
	UUID id,
	TranslationDto title,
	TranslationDto description,
	VisibilityStatus status,
	DifficultyLevel difficulty,
	ProgramingLanguage programingLanguage,
	boolean forceSequentialTasks,
	UserGeneralDataDto author,
	LanguageDto courseLanguage,
	boolean enrolled
) {
	public static CourseResponse fromEntity(@NotNull Course course, UserCourse userCourse) {
		return new CourseResponse(
			course.getId(),
			TranslationDto.fromEntity(
				DictionaryHelper.getTranslationWithCode(course.getTitle())
			),
			TranslationDto.fromEntity(
				DictionaryHelper.getTranslationWithCode(course.getDescription())
			),
			course.getStatus(),
			course.getDifficulty(),
			course.getProgramingLanguage(),
			course.isForceSequentialTasks(),
			UserGeneralDataDto.fromEntity(course.getAuthor()),
			LanguageDto.fromEntity(course.getTitle().getOriginalLanguage()),
			userCourse != null
		);
	}
}