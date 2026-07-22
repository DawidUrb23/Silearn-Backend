package com.itsilesia.si_learn.repositories.courses;


import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.repositories.SpecificationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends SpecificationJpaRepository<Task, UUID> {
}
