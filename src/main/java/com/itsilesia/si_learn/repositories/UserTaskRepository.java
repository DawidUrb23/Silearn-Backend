package com.itsilesia.si_learn.repositories;

import com.itsilesia.si_learn.entities.compositeIds.UserTaskId;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserTaskRepository extends JpaRepository<UserTask, UserTaskId> {

	Optional<UserTask> findByTaskIdAndUserId(UUID taskId, UUID userId);
	long deleteByTaskIdAndUserId(UUID taskId, UUID userId);
}
