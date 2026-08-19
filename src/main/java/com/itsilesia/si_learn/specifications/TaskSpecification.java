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

}
