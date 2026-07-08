package com.itsilesia.si_learn.repositories;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.FluentQuery;

import java.io.Serializable;
import java.util.function.Function;

/// Interface to allow execution of {@link Specification}s based on the JPA criteria API.<br>
/// JPA specific extension of {@link org.springframework.data.repository.Repository}.
@NoRepositoryBean
public interface SpecificationJpaRepository<T, ID extends Serializable>
	extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
	// Inherits:
	// - List<T> findAll() (from JpaRepository)
	// - List<T> findAll(Specification<T> spec) (from JpaSpecificationExecutor)


	// https://github.com/vladmihalcea/hypersistence-utils/issues/558#issuecomment-2329475307
	// all of this is just to avoid Page<T>.
	default Window<T> findAllWindowed(Specification<T> spec, Sort sort, int limit, ScrollPosition scrollPosition) {
		return this.findBy(spec, toWindow(sort, limit, scrollPosition));
	}

	default Window<T> findAllWindowed(Specification<T> spec, Sort sort, ScrollPosition scrollPosition) {
		return this.findBy(spec, toWindow(sort, scrollPosition));
	}

	default Window<T> findAllWindowed(Specification<T> spec, ScrollPosition scrollPosition) {
		return this.findAllWindowed(spec, Sort.unsorted(), scrollPosition);
	}

	default Slice<T> findAllSliced(Specification<T> spec, Pageable pageable) {
		final var window = pageable.isUnpaged()
			? this.findAllWindowed(spec, pageable.getSort(), ScrollPosition.offset())
			: this.findAllWindowed(spec, pageable.getSort(), pageable.getPageSize(), this.getInclusiveStartingOffset(pageable));
		return new SliceImpl<>(window.getContent(), pageable, window.hasNext());
	}

	private ScrollPosition getInclusiveStartingOffset(Pageable pageable) {
		return pageable.getOffset() == 0
			? ScrollPosition.offset()
			: ScrollPosition.offset(pageable.getOffset() - 1);
	}

	private static <T> Function<FluentQuery.FetchableFluentQuery<T>, Window<T>> toWindow(Sort sort, int limit, ScrollPosition scrollPosition) {
		return fetchableFluentQuery -> fetchableFluentQuery.sortBy(sort).limit(limit).scroll(scrollPosition);
	}

	private static <T> Function<FluentQuery.FetchableFluentQuery<T>, Window<T>> toWindow(Sort sort, ScrollPosition scrollPosition) {
		return fetchableFluentQuery -> fetchableFluentQuery.sortBy(sort).scroll(scrollPosition);
	}
}
