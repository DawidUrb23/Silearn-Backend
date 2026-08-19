package com.itsilesia.si_learn.repositories.user_progress;

import com.itsilesia.si_learn.entities.compositeIds.UserTaskId;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import com.itsilesia.si_learn.repositories.SpecificationJpaRepository;

public interface UserTaskRepository extends SpecificationJpaRepository<UserTask, UserTaskId> {
}
