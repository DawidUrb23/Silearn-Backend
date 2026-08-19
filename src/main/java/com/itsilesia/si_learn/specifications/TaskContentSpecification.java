package com.itsilesia.si_learn.specifications;

import com.itsilesia.si_learn.entities.BaseEntity_;
import com.itsilesia.si_learn.entities.courses.TaskContent;
import com.itsilesia.si_learn.entities.courses.TaskContent_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public class TaskContentSpecification extends CommonBaseSpecification<TaskContent> {
	public static Specification<TaskContent> hasTaskIdIn(List<UUID> taskIds) {
		return (root, query, criteriaBuilder) -> {
			if (taskIds == null || taskIds.isEmpty()) {
				return criteriaBuilder.disjunction(); // Always false -> returns empty list safely
			}
			return root.get(TaskContent_.TASK).get(BaseEntity_.ID).in(taskIds);
		};
	}

}
