package com.itsilesia.si_learn.repositories;

import com.itsilesia.si_learn.entities.courses.Module;

import java.util.UUID;

public interface ModuleRepository extends SpecificationJpaRepository<Module, UUID> {
}
