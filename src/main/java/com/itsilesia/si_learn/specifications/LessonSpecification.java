package com.itsilesia.si_learn.specifications;

import com.itsilesia.si_learn.entities.BaseEntity_;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.courses.Lesson_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public class LessonSpecification extends CommonBaseSpecification<Lesson> {
	public static Specification<Lesson> hasModuleIdIn(List<UUID> moduleIds) {
		return (root, query, criteriaBuilder) -> {
			if (moduleIds == null || moduleIds.isEmpty()) {
				return criteriaBuilder.disjunction(); // Always false -> returns empty list safely
			}
			return root.get(Lesson_.MODULE).get(BaseEntity_.ID).in(moduleIds);
		};
	}
}
