package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.abstractions.IndexedEntity;
import com.itsilesia.si_learn.entities.courses.Module;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ModuleWithLessonsResponse extends ModuleResponse {
	private List<LessonResponse> lessons;

	public ModuleWithLessonsResponse(@NotNull Module module) {
		super(module);
		this.lessons = module.getLessons() != null ? module.getLessons().stream()
			.sorted(Comparator.comparingInt(IndexedEntity::getIndex)) // Sorts ascending by Index
			.map(LessonResponse::fromEntity).toList() : List.of();
	}

	public static ModuleWithLessonsResponse fromEntity(@NotNull Module module) {
		return new ModuleWithLessonsResponse(module);
	}
}
