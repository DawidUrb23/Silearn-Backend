package com.itsilesia.si_learn.repositories;

import com.itsilesia.si_learn.entities.ProgramingLanguageCompiler;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgramingLanguageCompilerRepository extends SpecificationJpaRepository<ProgramingLanguageCompiler, UUID> {
	Optional<ProgramingLanguageCompiler> findByLanguage(ProgramingLanguage language);
}
