package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.dtos.response.dictonary.LanguageDto;
import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDto;
import com.itsilesia.si_learn.dtos.response.helper.DictionaryHelper;
import com.itsilesia.si_learn.dtos.response.users.UserGeneralDataDto;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.enums.DifficultyLevel;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.entities.enums.VisibilityStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public CourseResponse(@NotNull Course course) {
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
		this.createdAt = course.getCreatedAt();
		this.updatedAt = course.getUpdatedAt();
	}

	public static CourseResponse fromEntity(@NotNull Course course) {
		return new CourseResponse(course);
	}
}