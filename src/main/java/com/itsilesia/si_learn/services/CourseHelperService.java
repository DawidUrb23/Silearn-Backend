package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.entities.courses.*;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.exceptions.NotOwnerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true) // Keeps the Hibernate session alive to safely traverse lazy relations!
public class CourseHelperService {

	public void isAuthorOrThrow(Course course, UUID userId) {
		if (!Objects.equals(course.getAuthor().getId(), userId))
			throw new NotOwnerException(userId, course.getId());
	}

	public void isAuthorOrThrow(Module module, UUID userId) {
		isAuthorOrThrow(module.getCourse(), userId);
	}

	public void isAuthorOrThrow(Lesson lesson, UUID userId) {
		isAuthorOrThrow(lesson.getModule(), userId);
	}

	public void isAuthorOrThrow(Task task, UUID userId) {
		isAuthorOrThrow(task.getLesson(), userId);
	}

	public void isAuthorOrThrow(TaskContent taskContent, UUID userId) {
		isAuthorOrThrow(taskContent.getTask(), userId);
	}

	public void isAuthorOrThrow(TestScenario testScenario, UUID userId) {
		isAuthorOrThrow(testScenario.getTask(), userId);
	}

	public Language getCourseLanguage(Course course) {
		return course.getTitle().getOriginalLanguage();
	}

	public Language getCourseLanguage(Module module) {
		return getCourseLanguage(module.getCourse());
	}

	public Language getCourseLanguage(Lesson lesson) {
		return getCourseLanguage(lesson.getModule());
	}

	public Language getCourseLanguage(Task task) {
		return getCourseLanguage(task.getLesson());
	}

	public Language getCourseLanguage(TaskContent taskContent) {
		return getCourseLanguage(taskContent.getTask());
	}
}