package com.itsilesia.si_learn.repositories.user_progress;

import com.itsilesia.si_learn.entities.compositeIds.UserLessonId;
import com.itsilesia.si_learn.entities.user_progress.UserLesson;
import com.itsilesia.si_learn.repositories.SpecificationJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLessonRepository extends SpecificationJpaRepository<UserLesson, UserLessonId> {
}
