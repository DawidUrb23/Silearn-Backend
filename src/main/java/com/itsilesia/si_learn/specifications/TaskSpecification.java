package com.itsilesia.si_learn.specifications;

import com.itsilesia.si_learn.entities.BaseEntity_;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.courses.Task_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public class TaskSpecification {
	public static Specification<Task> hasLessonIdIn(List<UUID> lessonIds) {
		return (root, query, criteriaBuilder) -> {
			if (lessonIds == null || lessonIds.isEmpty()) {
				return criteriaBuilder.disjunction(); // Always false -> returns empty list safely
			}
			return root.get(Task_.LESSON).get(BaseEntity_.ID).in(lessonIds);
		};
	}

	public static Specification<Task> isAutoGraded() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isNotEmpty(root.get(Task_.TEST_SCENARIOS));
	}

	public static Specification<Task> isManuallyGraded() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.isEmpty(root.get(Task_.TEST_SCENARIOS));
	}
}
