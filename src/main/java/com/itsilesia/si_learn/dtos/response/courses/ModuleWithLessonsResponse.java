package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.entities.courses.Module;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ModuleWithLessonsResponse extends ModuleResponse {
	private List<LessonResponse> lessons;

	public ModuleWithLessonsResponse(@NotNull Module module) {
		super(module);
		this.lessons = module.getLessons() != null ? module.getLessons().stream().map(LessonResponse::fromEntity).toList() : List.of();
	}

	public static ModuleWithLessonsResponse fromEntity(@NotNull Module module) {
		return new ModuleWithLessonsResponse(module);
	}
}
