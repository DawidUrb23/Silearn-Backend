package com.itsilesia.si_learn.repositories.courses;

import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.repositories.SpecificationJpaRepository;

import java.util.UUID;

public interface ModuleRepository extends SpecificationJpaRepository<Module, UUID> {
}
