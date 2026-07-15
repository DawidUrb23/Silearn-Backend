package com.itsilesia.si_learn.repositories;

import com.itsilesia.si_learn.entities.compositeIds.UserLessonId;
import com.itsilesia.si_learn.entities.user_progress.UserLesson;

public interface UserLessonRepository extends SpecificationJpaRepository<UserLesson, UserLessonId> {
}
