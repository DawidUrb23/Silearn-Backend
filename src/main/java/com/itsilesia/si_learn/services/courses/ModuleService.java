package com.itsilesia.si_learn.services.courses;

import com.itsilesia.si_learn.dtos.request.courses.ContentsOrderUpdateRequest;
import com.itsilesia.si_learn.dtos.request.courses.CreateModuleRequest;
import com.itsilesia.si_learn.dtos.request.courses.ModuleUpdateRequest;
import com.itsilesia.si_learn.dtos.response.IdOnlyDto;
import com.itsilesia.si_learn.entities.abstractions.EntityWithOrderedChildren;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.courses.CourseRepository;
import com.itsilesia.si_learn.repositories.courses.ModuleRepository;
import com.itsilesia.si_learn.services.TranslationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModuleService {

	private final ModuleRepository moduleRepository;
	private final CourseRepository courseRepository;
	private final TranslationService translationService;
	private final CourseHelperService courseHelperService;

	@Transactional
	public Module createModule(UUID courseId, CreateModuleRequest request, UUID userId) {
		Course course = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));
		courseHelperService.isAuthorOrThrow(course, userId);
		var courseLanguage = courseHelperService.getOriginalLanguage(course);

		Module module = new Module();
		module.setTitle(translationService.createDictionary(request.title(), courseLanguage));
		module.setIndex(EntityWithOrderedChildren.getNextIndex(course.getModules()));
		module.setCourse(course);

		return moduleRepository.save(module);
	}

	@Transactional(readOnly = true)
	public Module getModuleWithLessons(UUID moduleId) {
		return moduleRepository.findById(moduleId)
			.orElseThrow(() -> new ItemNotFoundException(Module.class, moduleId));
	}

	@Transactional
	public Course updateModuleOrder(UUID courseId, @Valid ContentsOrderUpdateRequest request, UUID userId) {
		var toUpdate = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));
		courseHelperService.isAuthorOrThrow(toUpdate, userId);
		//update the indexes
		EntityWithOrderedChildren.updateIndexesOfChildren(toUpdate.getModules(), request);
		return courseRepository.save(toUpdate);
	}

	public void deleteModule(UUID moduleId, UUID userId) {
		Module module = moduleRepository.findById(moduleId)
			.orElseThrow(() -> new ItemNotFoundException(Module.class, moduleId));
		courseHelperService.isAuthorOrThrow(module, userId);

		moduleRepository.delete(module);
	}

	public ResponseEntity<IdOnlyDto> updateModuleByTeacher(UUID moduleId, UUID userId, ModuleUpdateRequest moduleUpdateRequest) {
		Module module = moduleRepository.findById(moduleId)
			.orElseThrow(() -> new ItemNotFoundException(Module.class, moduleId));
		courseHelperService.isAuthorOrThrow(module, userId);

		if (moduleUpdateRequest.getTitle() != null)
			translationService.updateOriginalTranslation(module.getTitle(), moduleUpdateRequest.getTitle());

		Module moduleSaved = moduleRepository.save(module);
		return ResponseEntity.ok(IdOnlyDto.fromEntity(moduleSaved));

	}
}