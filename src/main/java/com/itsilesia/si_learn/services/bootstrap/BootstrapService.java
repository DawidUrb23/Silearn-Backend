package com.itsilesia.si_learn.services.bootstrap;

import com.itsilesia.si_learn.services.LanguageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BootstrapService {
	private final LanguageService languageService;

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		addLanguages();
	}

	private void addLanguages() {
		if (languageService.findAll().isEmpty()) {
			languageService.addLanguage("pl");
			languageService.addLanguage("en");
		}
	}


}
