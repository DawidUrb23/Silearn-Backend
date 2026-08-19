package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.config.CacheConfig;
import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.exceptions.ItemAlreadyExistsException;
import com.itsilesia.si_learn.repositories.LanguageRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LanguageService {
	private final LanguageRepository languageRepository;

	@Cacheable(
		cacheNames = CacheConfig.languagesCache,
		sync = true
	)
	public List<Language> findAll() {
		return languageRepository.findAll();
	}

	private Optional<Language> getLanguageWithoutCache(@NotBlank String languageCode) {
		return languageRepository.findById(languageCode.toLowerCase());
	}

	@Transactional
	@CacheEvict(cacheNames = CacheConfig.languagesCache, allEntries = true)
	public Language addLanguage(@NotBlank String languageCode) {
		if (this.existsById(languageCode))
			throw new ItemAlreadyExistsException(Language.class, languageCode);
		Language language = new Language();
		language.setCode(languageCode.toLowerCase());
		return languageRepository.save(language);
	}

	private boolean existsById(@NotBlank String languageCode) {
		return languageRepository.existsById(languageCode.toLowerCase());
	}

	@CacheEvict(cacheNames = CacheConfig.languagesCache, allEntries = true)
	public Language save(Language lang) {
		return languageRepository.save(lang);
	}

	@Transactional
	@CacheEvict(cacheNames = CacheConfig.languagesCache, allEntries = true)
	public void deleteById(@NotBlank String languageCode) {
		languageRepository.deleteById(languageCode.toLowerCase());
	}
}
