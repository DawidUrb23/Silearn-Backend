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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CourseResponse {
	private UUID id;
	private TranslationDto title;
	private TranslationDto description;
	private VisibilityStatus status;
	private DifficultyLevel difficulty;
	private ProgramingLanguage programingLanguage;
	private boolean forceSequentialTasks;
	private UserGeneralDataDto author;
	private LanguageDto courseLanguage;
	private boolean enrolled;

	public CourseResponse(@NotNull Course course, UserCourse userCourse) {
		this.id = course.getId();
		this.title = TranslationDto.fromEntity(
			DictionaryHelper.getTranslationWithCode(course.getTitle())
		);
		this.description = TranslationDto.fromEntity(
			DictionaryHelper.getTranslationWithCode(course.getDescription())
		);
		this.status = course.getStatus();
		this.difficulty = course.getDifficulty();
		this.programingLanguage = course.getProgramingLanguage();
		this.forceSequentialTasks = course.isForceSequentialTasks();
		this.author = UserGeneralDataDto.fromEntity(course.getAuthor());
		this.courseLanguage = LanguageDto.fromEntity(course.getTitle().getOriginalLanguage());
		this.enrolled = userCourse != null;
	}

	public static CourseResponse fromEntity(@NotNull Course course, UserCourse userCourse) {
		return new CourseResponse(course, userCourse);
	}
}