package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDto;
import com.itsilesia.si_learn.dtos.response.helper.DictionaryHelper;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import jakarta.annotation.Nullable;
import lombok.*;

import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TaskResponse {
	private UUID id;
	private Float passingScore;
	private String startingCode;
	private ProgramingLanguage programingLanguage;
	private boolean isAutoGraded;
	@Nullable
	private TranslationDto name;

	public TaskResponse(Task task) {
		this.id = task.getId();
		this.startingCode = task.getStartingCode();
		this.passingScore = task.getPassingScore();
		this.programingLanguage = task.getCourse().getProgramingLanguage();
		this.isAutoGraded = !task.getTestScenarios().isEmpty();
		var nameNullable = Optional.ofNullable(task.getName());
		if (nameNullable.isPresent())
			this.name = TranslationDto.fromEntity(
				DictionaryHelper.getTranslationWithCode(nameNullable.get())
			);
		else {
			this.name = null;
		}
	}

	public static TaskResponse fromEntity(Task task) {
		return task == null ? null : new TaskResponse(task);
	}
}