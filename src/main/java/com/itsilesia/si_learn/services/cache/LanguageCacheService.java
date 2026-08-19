package com.itsilesia.si_learn.services.cache;

import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.services.LanguageService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LanguageCacheService {
	private final LanguageService languageService;

	public List<Language> getAllLanguages() {
		return languageService.findAll();
	}

	public Optional<Language> getLanguage(@NotBlank String languageCode) {
		return getAllLanguages().stream()
			.filter(language -> language.getCode().equalsIgnoreCase(languageCode))
			.findFirst();
	}
}
