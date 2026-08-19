package com.itsilesia.si_learn.specifications;

import com.itsilesia.si_learn.entities.BaseEntity_;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.entities.courses.Module_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public class ModuleSpecification extends CommonBaseSpecification<Module> {
	public static Specification<Module> hasCourseIdIn(List<UUID> courseIds) {
		return (root, query, criteriaBuilder) -> {
			if (courseIds == null || courseIds.isEmpty()) {
				return criteriaBuilder.disjunction(); // Always false -> returns empty list safely
			}
			return root.get(Module_.COURSE).get(BaseEntity_.ID).in(courseIds);
		};
	}
}
