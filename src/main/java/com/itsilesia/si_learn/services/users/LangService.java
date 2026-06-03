package com.itsilesia.si_learn.services.users;

import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.repositories.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LangService {
	private final LanguageRepository languageRepository;

	public Optional<Language> getLangByCode(String code) {
		return languageRepository.findById(code);
	}
}
