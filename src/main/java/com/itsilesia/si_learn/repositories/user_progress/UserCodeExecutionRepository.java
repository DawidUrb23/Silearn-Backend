package com.itsilesia.si_learn.repositories.user_progress;

import com.itsilesia.si_learn.entities.user_progress.UserCodeExecution;
import com.itsilesia.si_learn.repositories.SpecificationJpaRepository;

import java.util.UUID;

public interface UserCodeExecutionRepository extends SpecificationJpaRepository<UserCodeExecution, UUID> {
}
