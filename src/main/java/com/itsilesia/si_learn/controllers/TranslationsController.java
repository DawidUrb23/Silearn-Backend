package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.dictionary.AddLocaleRequest;
import com.itsilesia.si_learn.dtos.request.dictionary.UpdateTranslationDictionaryRequest;
import com.itsilesia.si_learn.dtos.response.IdOnlyDto;
import com.itsilesia.si_learn.dtos.response.dictonary.LanguageDto;
import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDictDto;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.services.TranslationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/translations")
@RequiredArgsConstructor
@Validated
@Tag(name = "Translations", description = "Endpoints for internationalization")
public class TranslationsController {
	private final TranslationService translationService;

	@GetMapping("languages")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Get all languages")
	public List<LanguageDto> getLanguages() {
		Locale currentLocale = LocaleContextHolder.getLocale(); //the Locale in which to display the `name` field
		return translationService.getLanguages()
			.stream().map(language -> LanguageDto.fromEntity(language, currentLocale))
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
	@Operation(summary = "Get translation dictionary", description = "Lists all translations of a dictionary")
	public TranslationDictDto getTranslationDictionary(
		@PathVariable UUID id
	) {
		return TranslationDictDto.fromEntity(
			translationService.getTranslationDictionary(id)
		);
	}

	@GetMapping("/courses/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Get all dictionaries used in course")
	public List<IdOnlyDto> getCourseDictionaries(
		@PathVariable UUID id
	) {
		return translationService.getCourseDictionaries(id)
			.stream().map(IdOnlyDto::fromEntity).toList();
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

}
