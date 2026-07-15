package com.itsilesia.si_learn.services;


import com.itsilesia.si_learn.entities.compositeIds.UserCourseId;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import com.itsilesia.si_learn.entities.users.User;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.CourseRepository;
import com.itsilesia.si_learn.repositories.UserCourseRepository;
import com.itsilesia.si_learn.repositories.users.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCourseService {

	private final CourseRepository courseRepository;
	private final UserRepository userRepository;
	private final UserCourseRepository userCourseRepository;

	public @NotNull List<UserCourse> getUserCourses(UUID userId) {
		return userCourseRepository.findAllByUser_Id(userId);
	}

	public UserCourse getUserCourse(UUID userId, UUID courseId) {
		return userCourseRepository.findById(new UserCourseId(userId, courseId))
			.orElseThrow(() -> new ItemNotFoundException(UserCourse.class, new UserCourseId(userId, courseId)));
	}

	public UserCourse enroll(UUID courseId, UUID userId) {
		var user = userRepository.findById(userId)
			.orElseThrow(() -> new ItemNotFoundException(User.class, userId));
		var course = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));
		var userCourse = new UserCourse();
		userCourse.setUser(user);
		userCourse.setCourse(course);

		return userCourseRepository.save(userCourse);
	}

	public UserCourse getUserCourseOrNull(UUID userId, UUID courseId) {
		if (userId == null) {
			return null;
		}

		return userCourseRepository.findById(new UserCourseId(userId, courseId))
			.orElse(null);
	}
}
