package com.itsilesia.si_learn.services.courses;

import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.entities.courses.*;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.exceptions.NotOwnerException;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true) // Keeps the Hibernate session alive to safely traverse lazy relations!
public class CourseHelperService {

	public void isAuthorOrThrow(@NonNull Course course, UUID userId) {
		if (userId == null || !Objects.equals(course.getAuthor().getId(), userId))
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

	public Language getOriginalLanguage(Course course) {
		return course.getTitle().getOriginalLanguage();
	}

	public Language getOriginalLanguage(Module module) {
		return module.getTitle().getOriginalLanguage();
	}

	public Language getOriginalLanguage(Lesson lesson) {
		return lesson.getTopic().getOriginalLanguage();
	}

	public Language getOriginalLanguage(Task task) {
		return getOriginalLanguage(task.getLesson());
	}

	public Language getOriginalLanguage(TaskContent taskContent) {
		return taskContent.getContent().getOriginalLanguage();
	}
}