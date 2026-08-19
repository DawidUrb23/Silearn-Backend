package com.itsilesia.si_learn.specifications;

import com.itsilesia.si_learn.entities.BaseEntity_;
import com.itsilesia.si_learn.entities.courses.Task_;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import com.itsilesia.si_learn.entities.user_progress.UserTask_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public class UserTaskSpecification {

	public static Specification<UserTask> hasTaskId(UUID taskId) {
		return (root, query, criteriaBuilder) -> {
			if (taskId == null) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.equal(root.get(UserTask_.TASK).get(Task_.ID), taskId);
		};
	}

	public static Specification<UserTask> hasTaskIdIn(List<UUID> taskIds) {
		return (root, query, criteriaBuilder) -> {
			if (taskIds == null || taskIds.isEmpty()) {
				return criteriaBuilder.disjunction(); // Always false -> returns empty list safely
			}
			return root.get(UserTask_.TASK).get(BaseEntity_.ID).in(taskIds);
		};
	}

	public static Specification<UserTask> hasUserSolutionNotEmpty() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.and(
			criteriaBuilder.isNotNull(root.get(UserTask_.USER_SOLUTION)),
			criteriaBuilder.notEqual(criteriaBuilder.trim(root.get(UserTask_.USER_SOLUTION)), "")
		);
	}

	public static Specification<UserTask> needsGrading() {
		return (root, query, criteriaBuilder) -> criteriaBuilder.or(
			// Solution was updated since the last grade
			criteriaBuilder.greaterThanOrEqualTo(
				root.get(UserTask_.UPDATED_SOLUTION_AT),
				root.get(UserTask_.GRADED_AT)
			),
			// Or solution has never been graded yet
			criteriaBuilder.isNull(root.get(UserTask_.GRADED_AT))
		);
	}

}
