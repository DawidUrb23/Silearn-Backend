package com.itsilesia.si_learn.services.courses;

import com.itsilesia.si_learn.dtos.request.courses.ContentsOrderUpdateRequest;
import com.itsilesia.si_learn.dtos.request.courses.CreateLessonRequest;
import com.itsilesia.si_learn.dtos.request.courses.UpdateLessonRequest;
import com.itsilesia.si_learn.dtos.response.IdOnlyDto;
import com.itsilesia.si_learn.entities.abstractions.EntityWithOrderedChildren;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.courses.LessonRepository;
import com.itsilesia.si_learn.repositories.courses.ModuleRepository;
import com.itsilesia.si_learn.services.TranslationService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
		var courseLang = courseHelperService.getOriginalLanguage(module);

		Lesson lesson = new Lesson();
		lesson.setTopic(translationService.createDictionary(request.topic(), courseLang));
		lesson.setDescription(translationService.createDictionary(request.description(), courseLang));
		lesson.setModule(module);
		lesson.setTasks(new ArrayList<>());
		lesson.setIndex(EntityWithOrderedChildren.getNextIndex(module.getLessons()));

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

	public Module updateLessonOrder(UUID moduleId, @Valid ContentsOrderUpdateRequest request, UUID userId) {
		var toUpdate = moduleRepository.findById(moduleId)
			.orElseThrow(() -> new ItemNotFoundException(Module.class, moduleId));
		courseHelperService.isAuthorOrThrow(toUpdate, userId);
		//update the indexes
		EntityWithOrderedChildren.updateIndexesOfChildren(toUpdate.getLessons(), request);
		return moduleRepository.save(toUpdate);
	}

	public ResponseEntity<IdOnlyDto> updateLesson(UUID lessonId, UUID userId, UpdateLessonRequest updateLessonRequest) {
		Lesson lesson = lessonRepository.findById(lessonId)
			.orElseThrow(() -> new ItemNotFoundException(Lesson.class, lessonId));
		courseHelperService.isAuthorOrThrow(lesson, userId);
		var lessonLang = courseHelperService.getOriginalLanguage(lesson);

		if(updateLessonRequest.getDescription() != null){
			lesson.setDescription(translationService.createDictionary(updateLessonRequest.getDescription(),lessonLang));
		}
		if(updateLessonRequest.getTopic() != null){
			lesson.setTopic(translationService.createDictionary(updateLessonRequest.getTopic(),lessonLang));
		}

		Lesson lessonSaved = lessonRepository.save(lesson);
		return ResponseEntity.ok(IdOnlyDto.fromEntity(lessonSaved));
	}
}
