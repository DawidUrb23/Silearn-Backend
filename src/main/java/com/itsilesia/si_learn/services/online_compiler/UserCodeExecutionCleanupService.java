package com.itsilesia.si_learn.services.online_compiler;

import com.itsilesia.si_learn.entities.BaseEntity_;
import com.itsilesia.si_learn.entities.user_progress.UserCodeExecution;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserCodeExecutionCleanupService {
	@PersistenceContext
	private final EntityManager entityManager;
	@Value("${api.onlinecompiler.cleanup.interval-minutes:10}")
	private long cleanupIntervalMinutes;


	/**
	 * Removes old user code compilations.
	 *
	 * @return amount of records removed
	 */
	@Transactional
	public long cleanupOutdated() {
		LocalDateTime cutoff = LocalDateTime.now().minusMinutes(cleanupIntervalMinutes);
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaDelete<UserCodeExecution> delete = cb.createCriteriaDelete(UserCodeExecution.class);
		Root<UserCodeExecution> root = delete.from(UserCodeExecution.class);
		delete.where(cb.lessThan(root.get(BaseEntity_.UPDATED_AT), cutoff));
		return entityManager.createQuery(delete).executeUpdate();
	}
}
