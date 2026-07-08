package com.itsilesia.si_learn.repositories;

import com.itsilesia.si_learn.entities.courses.TaskContent;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskContentRepository extends SpecificationJpaRepository<TaskContent, UUID> {
}
