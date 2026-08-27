package com.itsilesia.si_learn.services.admin;


import com.itsilesia.si_learn.config.CacheConfig;
import com.itsilesia.si_learn.dtos.request.admin.ProgramingLanguageCompilerUpsertRequest;
import com.itsilesia.si_learn.entities.ProgramingLanguageCompiler;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.ProgramingLanguageCompilerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgramingLanguageCompilerService {
	private final ProgramingLanguageCompilerRepository programingLanguageCompilerRepository;

	@Cacheable(value = CacheConfig.programingLanguageCompilersCache, key = "#root.methodName")
	public List<ProgramingLanguageCompiler> getAll() {
		return programingLanguageCompilerRepository.findAll();
	}

	@Cacheable(value = CacheConfig.programingLanguageCompilersCache)
	public ProgramingLanguageCompiler getById(ProgramingLanguage language) {
		return programingLanguageCompilerRepository.findByLanguage(language)
			.orElseThrow(() -> new ItemNotFoundException(ProgramingLanguageCompiler.class, language));
	}

	@CacheEvict(value = CacheConfig.programingLanguageCompilersCache, allEntries = true)
	public ProgramingLanguageCompiler update(ProgramingLanguageCompilerUpsertRequest request) {
		var programingLanguageCompiler = programingLanguageCompilerRepository.findByLanguage(request.getLanguage());
		if (programingLanguageCompiler.isEmpty()) {
			var newProgramingLanguageCompiler = new ProgramingLanguageCompiler();
			newProgramingLanguageCompiler.setLanguage(request.getLanguage());
			programingLanguageCompiler = Optional.of(newProgramingLanguageCompiler);
		}
		var update = programingLanguageCompiler.get();
		update.setCompiler(request.getCompiler());
		update.setCompilerNamePrefix(request.getCompilerNamePrefix());
		return programingLanguageCompilerRepository.save(update);
	}
}
