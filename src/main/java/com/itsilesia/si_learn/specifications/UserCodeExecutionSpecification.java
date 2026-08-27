package com.itsilesia.si_learn.specifications;

import com.itsilesia.si_learn.entities.BaseEntity_;
import com.itsilesia.si_learn.entities.enums.CompilationProgress;
import com.itsilesia.si_learn.entities.user_progress.UserCodeExecution;
import com.itsilesia.si_learn.entities.user_progress.UserCodeExecution_;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class UserCodeExecutionSpecification extends CommonBaseSpecification<UserCodeExecution> {
	public static Specification<UserCodeExecution> hasUser(UUID userId) {
		return (root, query, criteriaBuilder) -> {
			if (userId == null)
				return criteriaBuilder.conjunction();
			return criteriaBuilder.equal(root.get(UserCodeExecution_.USER).get(BaseEntity_.ID), userId);
		};
	}

	public static Specification<UserCodeExecution> hasTask(UUID taskId) {
		return (root, query, criteriaBuilder) -> {
			if (taskId == null)
				return criteriaBuilder.conjunction();
			return criteriaBuilder.equal(root.get(UserCodeExecution_.TASK).get(BaseEntity_.ID), taskId);
		};
	}

	public static Specification<UserCodeExecution> hasCompilationId(UUID compilationId) {
		return (root, query, criteriaBuilder) -> {
			if (compilationId == null)
				return criteriaBuilder.conjunction();
			return criteriaBuilder.equal(root.get(UserCodeExecution_.COMPILATION_ID), compilationId);
		};
	}

	public static Specification<UserCodeExecution> hasTestScenario(UUID testScenarioId) {
		return (root, query, criteriaBuilder) -> {
			if (testScenarioId == null)
				return criteriaBuilder.conjunction();
			return criteriaBuilder.equal(root.get(UserCodeExecution_.TEST_SCENARIO).get(BaseEntity_.ID), testScenarioId);
		};
	}

	public static Specification<UserCodeExecution> hasCompilationProgress(CompilationProgress compilationProgress) {
		return (root, query, criteriaBuilder) -> {
			if (compilationProgress == null)
				return criteriaBuilder.conjunction();
			return criteriaBuilder.equal(root.get(UserCodeExecution_.COMPILATION_PROGRESS), compilationProgress);
		};
	}

	public static Specification<UserCodeExecution> hasUserTask(UserTask userTask) {
		return (root, query, criteriaBuilder) -> {
			if (userTask == null)
				return criteriaBuilder.conjunction();
			return criteriaBuilder.equal(root.get(UserCodeExecution_.USER_TASK), userTask);
		};
	}

	public static Specification<UserCodeExecution> hasGroupId(UUID groupId) {
		return (root, query, criteriaBuilder) -> {
			if (groupId == null)
				return criteriaBuilder.conjunction();
			return criteriaBuilder.equal(root.get(UserCodeExecution_.GROUP_ID), groupId);
		};
	}

	public static Specification<UserCodeExecution> anyPendingInGroupId(UUID groupId) {
		return (root, query, criteriaBuilder) -> {
			if (groupId == null)
				return criteriaBuilder.disjunction();
			return criteriaBuilder.and(
				criteriaBuilder.equal(root.get(UserCodeExecution_.GROUP_ID), groupId),
				criteriaBuilder.equal(root.get(UserCodeExecution_.COMPILATION_PROGRESS), CompilationProgress.PENDING)
			);
		};
	}
}
