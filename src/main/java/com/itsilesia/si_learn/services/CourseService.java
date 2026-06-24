package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.courses.CreateCourseRequest;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.users.User;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.CourseRepository;
import com.itsilesia.si_learn.repositories.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

	private final CourseRepository courseRepository;
	private final UserRepository userRepository;
	private final TranslationService translationService;

	@Transactional
	public Course createCourse(CreateCourseRequest request, UUID authorId) {
		User author = userRepository.findById(authorId)
			.orElseThrow(() -> new ItemNotFoundException(User.class, authorId));

		Course course = new Course();
		course.setTitle(translationService.createDictionary(request.title(), request.languageCode()));
		course.setDescription(translationService.createDictionary(request.description(), request.languageCode()));
		course.setAuthor(author);
		course.setStatus(request.status());
		course.setDifficulty(request.difficulty());
		course.setProgramingLanguage(request.programingLanguage());
		course.setForceSequentialTasks(request.forceSequentialTasks());

		return courseRepository.save(course);
	}

	public Course getCourse(UUID courseId) {
		return courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));
	}
}