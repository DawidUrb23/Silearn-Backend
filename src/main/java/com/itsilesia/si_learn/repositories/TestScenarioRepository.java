package com.itsilesia.si_learn.repositories;

import com.itsilesia.si_learn.entities.courses.TestScenario;

import java.util.List;
import java.util.UUID;

public interface TestScenarioRepository extends SpecificationJpaRepository<TestScenario, UUID> {
	List<TestScenario> findAllByTaskId(UUID id);

}
