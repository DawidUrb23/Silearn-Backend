package com.itsilesia.si_learn.exceptions;

import com.itsilesia.si_learn.dtos.response.helper.DictionaryHelper;
import com.itsilesia.si_learn.entities.abstractions.CourseAccessNode;
import com.itsilesia.si_learn.entities.courses.Course;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/// Not Authorized to do this (400)
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CourseIsSequentialException extends RuntimeException {
	private final Course course;
	private final CourseAccessNode resource;

	public CourseIsSequentialException(Course course, CourseAccessNode resource) {
		super(String.format("Course \"%s\": \"%s\" is sequential, user can not access the resource \"%s\" with id \"%s\"",
			course.getId(),
			DictionaryHelper.getTranslationWithCode(course.getTitle()).getValue(),
			resource.getClass().getSimpleName(),
			resource.getId()
		));
		this.course = course;
		this.resource = resource;
	}
}
