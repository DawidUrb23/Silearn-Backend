package com.itsilesia.si_learn.specyfications;

import com.itsilesia.si_learn.entities.enums.Role;
import com.itsilesia.si_learn.entities.users.User;
import com.itsilesia.si_learn.entities.users.UserRole_;
import com.itsilesia.si_learn.entities.users.User_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

	public static Specification<User> hasRole(Role role) {
		return (root, query, criteriaBuilder) -> {
			if (role == null) {
				return criteriaBuilder.conjunction(); // No filtering, return all
			}
			Join<Object, Object> rolesJoin = root.join(User_.ROLES);
			return criteriaBuilder.equal(rolesJoin.get(UserRole_.ROLE), role);
		};
	}
}
