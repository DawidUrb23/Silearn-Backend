package com.itsilesia.si_learn.repositories;

import com.itsilesia.si_learn.entities.compositeIds.UserCourseId;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseRepository extends SpecificationJpaRepository<UserCourse, UserCourseId> {
}
