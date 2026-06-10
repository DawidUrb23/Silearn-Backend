package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.exceptions.ItemAlreadyExistsException;
import com.itsilesia.si_learn.repositories.LanguageRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LanguageService {
	private final LanguageRepository languageRepository;

	public List<Language> getAll() {
		return languageRepository.findAll();
	}

	public Optional<Language> getLanguage(@NotBlank String languageCode) {
		return languageRepository.findById(languageCode.toLowerCase());
	}

	public Language addLanguage(@NotBlank String languageCode) {
		getLanguage(languageCode).ifPresent(lang -> {
			throw new ItemAlreadyExistsException(Language.class, languageCode);
		});
		Language language = new Language();
		language.setCode(languageCode.toLowerCase());
		return languageRepository.save(language);
	}
}
