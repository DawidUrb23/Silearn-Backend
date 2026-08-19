package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.dictionary.AddLocaleRequest;
import com.itsilesia.si_learn.dtos.request.dictionary.GetUntranslatedDictionariesFilters;
import com.itsilesia.si_learn.dtos.request.dictionary.UpdateTranslationDictionaryRequest;
import com.itsilesia.si_learn.dtos.request.dictionary.UpdateTranslationsInDictionaryRequest;
import com.itsilesia.si_learn.dtos.response.courses.KeysetTranslationDictionaryResponse;
import com.itsilesia.si_learn.dtos.response.dictonary.LanguageDto;
import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDictDto;
import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDto;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.helpers.cursors.CourseCursor;
import com.itsilesia.si_learn.helpers.cursors.CursorUtil;
import com.itsilesia.si_learn.services.TranslationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/translations")
@RequiredArgsConstructor
@Validated
@Tag(name = "Translations", description = "Endpoints for internationalization (Translator mainly)")
public class TranslationsController {
	private final TranslationService translationService;

	@GetMapping("languages")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Get all languages (Public)")
	public List<LanguageDto> getLanguages(
		@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String languageCode  //exposes the header for LocaleContextHolder
	) {
		return translationService.getLanguages()
			.stream().map(LanguageDto::fromEntity)
			.toList();
	}

	@PostMapping("languages")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Operation(summary = "Add a language (Admin)", description = "Adds a new language to the dictionary")
	@ApiResponse(responseCode = "400", description = "Invalid input, invalid language code",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "409", description = "Language already exists",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void addLanguage(
		@Valid @RequestBody AddLocaleRequest request
	) {
		translationService.addLocale(request.getLanguageCode());
	}

	@DeleteMapping("languages")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Operation(summary = "Remove a language (Admin)", description = "Removes a language from the dictionary (only if it is not used)")
	@ApiResponse(responseCode = "400", description = "Invalid input, invalid language code",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void removeLanguage(
		@Valid @RequestBody AddLocaleRequest request
	) {
		translationService.removeLocale(request.getLanguageCode());
	}

	@GetMapping("/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Get translation dictionary (Public)", description = "Lists all translations of a dictionary")
	public TranslationDictDto getTranslationDictionary(
		@PathVariable UUID id
	) {
		return TranslationDictDto.fromEntity(
			translationService.getTranslationDictionary(id)
		);
	}

	@PutMapping("/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Operation(summary = "Update translation dictionary (Translator)", description = "Add or update a translation of a dictionary")
	@ApiResponse(responseCode = "404", description = "requested item does not exist",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void updateTranslationDictionary(
		@PathVariable UUID id,
		@Valid @RequestBody UpdateTranslationDictionaryRequest request
	) {
		translationService.updateTranslationDictionary(id, request);
	}

	@PostMapping("/{id}/translations")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Operation(summary = "Update translation dictionary with all translations (Translator)", description = "Add or update a translation of a dictionary")
	@ApiResponse(responseCode = "404", description = "requested item does not exist",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void updateTranslationsInDictionary(
		@PathVariable UUID id,
		@Valid @RequestBody UpdateTranslationsInDictionaryRequest request
	) {
		translationService.updateTranslationsInDictionary(id, request);
	}

	@GetMapping("/courses/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Get all dictionaries used in course (Translator)")
	public List<UUID> getCourseDictionaries(
		@PathVariable UUID id,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "50") int size
	) {
		return translationService.getCourseDictionaryIds(id, cursor, size);
	}

	@GetMapping("/not-translated")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Get all dictionaries that are missing an up to date translation (Translator)")
	public KeysetTranslationDictionaryResponse getUntranslatedDictionaries(
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "10") int size,
		@ModelAttribute GetUntranslatedDictionariesFilters filters
	) {
		var languages = translationService.getLanguages();
		var slice = translationService.GetTranslationDictionariesThatNeedTranslating(
			cursor, size, filters);
		return getKeysetTranslationDictionaryResponse(slice, languages);
	}

	@GetMapping("/not-translated-to")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Get all dictionaries that are missing a translation to chosen language (Translator)")
	public KeysetTranslationDictionaryResponse getUntranslatedToLanguageDictionaries(
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = true) String missingLanguageCode,
		@ModelAttribute GetUntranslatedDictionariesFilters filters
	) {
		var languages = translationService.getLanguages();
		var slice = translationService.GetTranslationDictionariesThatNeedTranslatingInto(
			cursor, size, filters, missingLanguageCode);
		return getKeysetTranslationDictionaryResponse(slice, languages);
	}


	private static @NonNull KeysetTranslationDictionaryResponse getKeysetTranslationDictionaryResponse(Slice<@NonNull TranslationDictionary> slice, List<Language> languages) {
		String nextCursor = null;
		if (slice.hasNext() && !slice.getContent().isEmpty()) {
			var lastItem = slice.getContent().getLast();
			nextCursor = CursorUtil.encode(new CourseCursor(lastItem.getCreatedAt(), lastItem.getId()));
		}

		var data = slice.getContent().stream()
			.map(TranslationDto::fromEntity)
			.toList();
		return new KeysetTranslationDictionaryResponse(data,
			languages.stream().map(LanguageDto::fromEntity).toList(),
			nextCursor);
	}

}
