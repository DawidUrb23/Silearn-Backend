package com.itsilesia.si_learn.repositories;

import com.itsilesia.si_learn.entities.TranslationDictionary;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TranslationDictionaryRepository extends SpecificationJpaRepository<TranslationDictionary, UUID> {
}
