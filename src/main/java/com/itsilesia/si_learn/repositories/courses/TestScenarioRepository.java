package com.itsilesia.si_learn.repositories.courses;

import com.itsilesia.si_learn.entities.courses.TestScenario;
import com.itsilesia.si_learn.repositories.SpecificationJpaRepository;

import java.util.List;
import java.util.UUID;

public interface TestScenarioRepository extends SpecificationJpaRepository<TestScenario, UUID> {
	List<TestScenario> findAllByTaskId(UUID id);
	boolean existsByTaskId(UUID id);
}
