package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.courses.CreateLessonRequest;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.LessonRepository;
import com.itsilesia.si_learn.repositories.ModuleRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonService {
	private final LessonRepository lessonRepository;
	private final ModuleRepository moduleRepository;
	private final TranslationService translationService;
	private final CourseHelperService courseHelperService;

	@Transactional
	public Lesson createLesson(UUID moduleId, CreateLessonRequest request, UUID userId) {
		Module module = moduleRepository.findById(moduleId)
			.orElseThrow(() -> new ItemNotFoundException(Module.class, moduleId));
		courseHelperService.isAuthorOrThrow(module, userId);
		var courseLang = courseHelperService.getCourseLanguage(module);

		Lesson lesson = new Lesson();
		lesson.setTopic(translationService.createDictionary(request.topic(), courseLang));
		lesson.setDescription(translationService.createDictionary(request.description(), courseLang));
		lesson.setModule(module);
		lesson.setTasks(new ArrayList<>());
		lesson.setIndex(request.index());

		return lessonRepository.save(lesson);
	}

	public @NotNull Lesson getLessonWithContent(UUID lessonId) {
		return lessonRepository.findById(lessonId)
			.orElseThrow(() -> new ItemNotFoundException(Lesson.class, lessonId));
	}
	public void deleteLesson(UUID lessonId) {
		Lesson lesson = lessonRepository.findById(lessonId)
			.orElseThrow(() -> new ItemNotFoundException(Lesson.class, lessonId));

		lessonRepository.delete(lesson);
	}
}
