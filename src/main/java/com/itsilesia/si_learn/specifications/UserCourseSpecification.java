package com.itsilesia.si_learn.specifications;

import com.itsilesia.si_learn.entities.courses.Course_;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import com.itsilesia.si_learn.entities.user_progress.UserCourse_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class UserCourseSpecification {

	public static Specification<UserCourse> hasCourseId(UUID courseId) {
		return (root, query, cb) -> {
			if (courseId == null)
				return cb.conjunction();
			return cb.equal(root.get(UserCourse_.course).get(Course_.id), courseId);
		};
	}

	public static Specification<UserCourse> registeredIn(LocalDate fromDate, LocalDate toDate) {
		return (root, query, cb) -> {
			if (fromDate == null && toDate == null)
				return cb.conjunction();
			if (fromDate != null && toDate != null)
				return cb.between(root.get(UserCourse_.startDate), fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));
			if (fromDate != null)
				return cb.greaterThanOrEqualTo(root.get(UserCourse_.startDate), fromDate.atStartOfDay());
			return cb.lessThanOrEqualTo(root.get(UserCourse_.startDate), toDate.atTime(LocalTime.MAX));
		};
	}

	public static Specification<UserCourse> completedIn(LocalDate fromDate, LocalDate toDate) {
		return (root, query, cb) -> {
			var isNotNull = cb.isNotNull(root.get(UserCourse_.endDate));
			if (fromDate == null && toDate == null)
				return isNotNull;
			if (fromDate != null && toDate != null)
				return cb.and(isNotNull, cb.between(root.get(UserCourse_.endDate), fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX)));
			if (fromDate != null)
				return cb.and(isNotNull, cb.greaterThanOrEqualTo(root.get(UserCourse_.endDate), fromDate.atStartOfDay()));
			return cb.and(isNotNull, cb.lessThanOrEqualTo(root.get(UserCourse_.endDate), toDate.atTime(LocalTime.MAX)));
		};
	}

	public static Specification<UserCourse> inProgressIn(LocalDate fromDate, LocalDate toDate) {
		return (root, query, cb) -> {
			var isNull = cb.isNull(root.get(UserCourse_.endDate));
			if (fromDate == null && toDate == null)
				return cb.and(isNull, cb.isNotNull(root.get(UserCourse_.startDate)));
			if (fromDate != null && toDate != null)
				return cb.and(isNull, cb.between(root.get(UserCourse_.startDate), fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX)));
			if (fromDate != null)
				return cb.and(isNull, cb.greaterThanOrEqualTo(root.get(UserCourse_.startDate), fromDate.atStartOfDay()));
			return cb.and(isNull, cb.lessThanOrEqualTo(root.get(UserCourse_.startDate), toDate.atTime(LocalTime.MAX)));
		};
	}
}
