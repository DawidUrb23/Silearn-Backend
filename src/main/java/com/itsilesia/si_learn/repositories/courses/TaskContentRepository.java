package com.itsilesia.si_learn.repositories.courses;

import com.itsilesia.si_learn.entities.courses.TaskContent;
import com.itsilesia.si_learn.repositories.SpecificationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskContentRepository extends SpecificationJpaRepository<TaskContent, UUID> {
}
