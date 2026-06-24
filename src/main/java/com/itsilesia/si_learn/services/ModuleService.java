package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.courses.CreateModuleRequest;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.CourseRepository;
import com.itsilesia.si_learn.repositories.ModuleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModuleService {

	private final ModuleRepository moduleRepository;
	private final CourseRepository courseRepository;
	private final TranslationService translationService;

	@Transactional
	public Module createModule(UUID courseId, CreateModuleRequest request) {
		Course course = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));

		Module module = new Module();
		module.setTitle(translationService.createDictionary(request.title(), request.languageCode()));
		module.setIndex(request.index());
		module.setCourse(course);

		return moduleRepository.save(module);
	}
	public Module getModuleWithLessons(UUID moduleId) {
		return moduleRepository.findById(moduleId)
			.orElseThrow(() -> new ItemNotFoundException(Module.class, moduleId));
	}
}