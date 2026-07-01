package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.courses.CreateModuleRequest;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.CourseRepository;
import com.itsilesia.si_learn.repositories.ModuleRepository;
import lombok.RequiredArgsConstructor;
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
		var courseLanguage = courseHelperService.getCourseLanguage(course);

		Module module = new Module();
		module.setTitle(translationService.createDictionary(request.title(), courseLanguage));
		module.setIndex(request.index());
		module.setCourse(course);

		return moduleRepository.save(module);
	}

	@Transactional(readOnly = true)
	public Module getModuleWithLessons(UUID moduleId) {
		return moduleRepository.findById(moduleId)
			.orElseThrow(() -> new ItemNotFoundException(Module.class, moduleId));
	}
}