package com.itsilesia.si_learn.repositories.user_progress;

import com.itsilesia.si_learn.entities.compositeIds.UserCourseId;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import com.itsilesia.si_learn.repositories.SpecificationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserCourseRepository extends SpecificationJpaRepository<UserCourse, UserCourseId> {
	List<UserCourse> findAllByUser_Id(UUID userId);
}
