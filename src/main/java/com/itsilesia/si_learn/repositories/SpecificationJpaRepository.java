package com.itsilesia.si_learn.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/// Interface to allow execution of {@link Specification}s based on the JPA criteria API.<br>
/// JPA specific extension of {@link org.springframework.data.repository.Repository}.
@NoRepositoryBean
public interface SpecificationJpaRepository<T, ID extends Serializable>
	extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
	// Inherits:
	// - List<T> findAll() (from JpaRepository)
	// - List<T> findAll(Specification<T> spec) (from JpaSpecificationExecutor)
}
