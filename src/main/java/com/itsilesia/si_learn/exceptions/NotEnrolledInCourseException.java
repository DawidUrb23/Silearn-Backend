package com.itsilesia.si_learn.exceptions;

import com.itsilesia.si_learn.dtos.response.helper.DictionaryHelper;
import com.itsilesia.si_learn.entities.courses.Course;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/// Not Authorized to do this (403)
@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotEnrolledInCourseException extends Base403StatusException {
	private final UUID userId;
	private final Course course;

	public NotEnrolledInCourseException(UUID userId, Course course) {
		super(String.format("User \"%s\" is not enrolled to \"%s\": \"%s\"", userId, course.getId(),
			DictionaryHelper.getTranslationWithCode(course.getTitle()).getValue()));
		this.userId = userId;
		this.course = course;
	}
}
