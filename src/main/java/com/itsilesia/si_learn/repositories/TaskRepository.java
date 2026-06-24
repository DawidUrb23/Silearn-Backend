package com.itsilesia.si_learn.repositories;


import com.itsilesia.si_learn.entities.courses.Task;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends SpecificationJpaRepository<Task, UUID> {
}
