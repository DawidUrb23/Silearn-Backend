package com.itsilesia.si_learn.exceptions;

import com.itsilesia.si_learn.dtos.response.helper.DictionaryHelper;
import com.itsilesia.si_learn.entities.courses.Course;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/// bad -course is not accessible (400)
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CourseNotAccessibleException extends Base400StatusException {
	private final Course course;

	public CourseNotAccessibleException(Course course) {
		super(String.format("Course \"%s\": \"%s\" is not accessible!", course.getId(),
			DictionaryHelper.getTranslationWithCode(course.getTitle()).getValue()));
		this.course = course;
	}
}
