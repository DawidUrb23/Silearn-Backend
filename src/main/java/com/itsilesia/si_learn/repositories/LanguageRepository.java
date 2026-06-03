package com.itsilesia.si_learn.repositories;

import com.itsilesia.si_learn.entities.Language;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends SpecificationJpaRepository<Language, String> {
}
