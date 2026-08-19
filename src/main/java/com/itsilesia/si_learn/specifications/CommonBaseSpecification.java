package com.itsilesia.si_learn.specifications;

import com.itsilesia.si_learn.entities.BaseEntity;
import com.itsilesia.si_learn.entities.BaseEntity_;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public abstract class CommonBaseSpecification<TClass extends BaseEntity> {
	public Specification<TClass> isBefore(LocalDateTime createdAt, UUID id) {
		return (root, query, criteriaBuilder) -> {
			if (createdAt == null || id == null) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.or(
				criteriaBuilder.lessThan(root.get(BaseEntity_.CREATED_AT), createdAt),
				criteriaBuilder.and(
					criteriaBuilder.equal(root.get(BaseEntity_.CREATED_AT), createdAt),
					criteriaBuilder.lessThan(root.get(BaseEntity_.ID), id)
				)
			);
		};
	}

	public Specification<TClass> orderByCreatedAtDateId(Sort.Direction orderDirection) {
		return (root, query, criteriaBuilder) -> {
			if (orderDirection.isAscending()) {
				query.orderBy(
					criteriaBuilder.asc(root.get(BaseEntity_.CREATED_AT)),
					criteriaBuilder.asc(root.get(BaseEntity_.ID))
				);
			} else {
				query.orderBy(
					criteriaBuilder.desc(root.get(BaseEntity_.CREATED_AT)),
					criteriaBuilder.desc(root.get(BaseEntity_.ID))
				);
			}
			return criteriaBuilder.conjunction();
		};
	}

	public Specification<TClass> hasId(UUID id) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(BaseEntity_.ID), id);
	}

	public Specification<TClass> hasIdIn(List<UUID> ids) {
		return (root, query, criteriaBuilder) -> {
			if (ids == null || ids.isEmpty()) {
				return criteriaBuilder.disjunction(); // Always false -> returns empty list safely
			}
			return root.get(BaseEntity_.ID).in(ids);
		};
	}

	public Specification<TClass> createdBefore(LocalDateTime createdBefore) {
		return (root, query, criteriaBuilder) -> {
			if (createdBefore == null)
				return criteriaBuilder.conjunction();
			return criteriaBuilder.lessThan(root.get(BaseEntity_.CREATED_AT), createdBefore);
		};
	}

	public Specification<TClass> createdAfter(LocalDateTime createdAfter) {
		return (root, query, criteriaBuilder) -> {
			if (createdAfter == null)
				return criteriaBuilder.conjunction();
			return criteriaBuilder.greaterThan(root.get(BaseEntity_.CREATED_AT), createdAfter);
		};
	}

	public Specification<TClass> updateBefore(LocalDateTime updateBefore) {
		return (root, query, criteriaBuilder) -> {
			if (updateBefore == null)
				return criteriaBuilder.conjunction();
			return criteriaBuilder.lessThan(root.get(BaseEntity_.UPDATED_AT), updateBefore);
		};
	}

	public Specification<TClass> updateAfter(LocalDateTime updateAfter) {
		return (root, query, criteriaBuilder) -> {
			if (updateAfter == null)
				return criteriaBuilder.conjunction();
			return criteriaBuilder.greaterThan(root.get(BaseEntity_.UPDATED_AT), updateAfter);
		};
	}
}
