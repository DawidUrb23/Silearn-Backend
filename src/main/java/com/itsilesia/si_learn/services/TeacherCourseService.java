package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.response.courses.CourseStatisticsDto;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.users.User;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.courses.CourseRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserCourseRepository;
import com.itsilesia.si_learn.repositories.users.UserRepository;
import com.itsilesia.si_learn.services.courses.CourseHelperService;
import com.itsilesia.si_learn.specifications.UserCourseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherCourseService {

	private final CourseRepository courseRepository;
	private final UserRepository userRepository;
	private final UserCourseRepository userCourseRepository;
	private final CourseHelperService courseHelperService;

	public List<CourseStatisticsDto> getCoursesStatistics(UUID teacherId, LocalDate fromDate, LocalDate toDate) {
		User user = userRepository.findById(teacherId)
			.orElseThrow(() -> new ItemNotFoundException(User.class, teacherId));

		List<Course> courses = user.getAuthoredCourses().stream()
			.sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
			.toList();

		return courses.stream()
			.map(course -> getCourseStatistics(course, fromDate, toDate))
			.toList();
	}

	public CourseStatisticsDto getCourseStatisticsById(UUID courseId, UUID teacherId, LocalDate fromDate, LocalDate toDate) {
		Course course = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));
		courseHelperService.isAuthorOrThrow(course, teacherId);
		return getCourseStatistics(course, fromDate, toDate);
	}

	private CourseStatisticsDto getCourseStatistics(Course course, LocalDate fromDate, LocalDate toDate) {
		UUID courseId = course.getId();

		long registered = userCourseRepository.count(
			Specification.where(UserCourseSpecification.hasCourseId(courseId))
				.and(UserCourseSpecification.registeredIn(fromDate, toDate))
		);

		long completed = userCourseRepository.count(
			Specification.where(UserCourseSpecification.hasCourseId(courseId))
				.and(UserCourseSpecification.completedIn(fromDate, toDate))
		);

		long inProgress = userCourseRepository.count(
			Specification.where(UserCourseSpecification.hasCourseId(courseId))
				.and(UserCourseSpecification.inProgressIn(fromDate, toDate))
		);

		return new CourseStatisticsDto(course, registered, completed, inProgress);
	}
}
