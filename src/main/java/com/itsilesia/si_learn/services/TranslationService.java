package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.dictionary.GetUntranslatedDictionariesFilters;
import com.itsilesia.si_learn.dtos.request.dictionary.UpdateTranslationDictionaryRequest;
import com.itsilesia.si_learn.dtos.request.dictionary.UpdateTranslationsInDictionaryRequest;
import com.itsilesia.si_learn.entities.DictionaryItem;
import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.entities.enums.Translatable;
import com.itsilesia.si_learn.exceptions.InvalidParametersException;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.helpers.cursors.CursorUtil;
import com.itsilesia.si_learn.helpers.cursors.TranslationDictionaryCursor;
import com.itsilesia.si_learn.repositories.TranslationDictionaryRepository;
import com.itsilesia.si_learn.services.cache.CourseDictionaryCacheService;
import com.itsilesia.si_learn.services.cache.LanguageCacheService;
import com.itsilesia.si_learn.specifications.TranslationDictionarySpecification;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranslationService {
	private final LanguageService languageService;
	private final LanguageCacheService languageCacheService;
	private final TranslationDictionaryRepository translationDictionaryRepository;
	private final CourseDictionaryCacheService courseDictionaryCacheService;
	static private final int maxSizeFetchable = 100;
	static private final int maxSizeReturnable = 200;

	/**
	 * Creates a dictionary entry with requested original language. This function does NOT save to DB.
	 *
	 * @param value        text to be internationalized
	 * @param language     the original language of the text
	 * @param translatable defines that this text should be  translated (false for code blocks) and how
	 * @return a new TranslationDictionary with entry in original language
	 */
	@Transactional
	public TranslationDictionary createDictionary(@NotNull String value, @NotNull Language language, Translatable translatable) {
		TranslationDictionary dictionary = new TranslationDictionary();
		dictionary.setOriginalLanguage(language);
		dictionary.setTranslatable(translatable);
		this.dictionaryNeedsReTranslation(dictionary, true);

		DictionaryItem item = new DictionaryItem();
		item.setLanguage(language);
		item.setValue(value);
		item.setTranslationDictionary(dictionary);

		dictionary.getItems().add(item);

		return dictionary;
	}

	private void dictionaryNeedsReTranslation(TranslationDictionary dictionary, Boolean hintForceUpdate) {
		var translatable = dictionary.getTranslatable();
		//not translatable
		if (translatable == Translatable.NotTranslatable) {
			dictionary.setNeedsReTranslation(false);
			return;
		}
		//hinted that there was a change invalidating the translations (updateOriginalTranslation, createDictionary)
		if (hintForceUpdate == true) {
			dictionary.setNeedsReTranslation(true);
			return;
		}
		//check if there is a need to update it
		var languages = this.getLanguages();
		var dictItems = dictionary.getItems();
		//not all translations are present
		if (dictItems.size() < languages.size()) {
			dictionary.setNeedsReTranslation(true);
			return;
		}
		var originalLang = dictionary.getOriginalLanguage();
		var origItemUpdateAt = dictionary.getItems().stream().filter(item -> item.getLanguage().equals(originalLang)).findFirst().orElseThrow().getUpdatedAt();

		//not all translations are up to date
		if (dictItems.stream().anyMatch(item -> item.getUpdatedAt().isBefore(origItemUpdateAt))) {
			dictionary.setNeedsReTranslation(true);
			return;
		}
		//all translations are up to date
		dictionary.setNeedsReTranslation(false);
	}

	/**
	 * Creates a dictionary entry with requested original language(sets content to auto translatable). This function does NOT save to DB.
	 *
	 * @param value    text to be internationalized
	 * @param language the original language of the text
	 * @return a new TranslationDictionary with entry in original language
	 */
	@Transactional
	public TranslationDictionary createDictionary(@NotNull String value, @NotNull Language language) {
		return this.createDictionary(value, language, Translatable.AutoTranslation);
	}

	/**
	 * Creates a dictionary entry with requested original language. This function does NOT save to DB.
	 *
	 * @param value        text to be internationalized
	 * @param languageCode the original language of the text
	 * @param translatable defines that this text should be  translated (false for code blocks) and how
	 * @return a new TranslationDictionary with entry in original language
	 */
	@Transactional
	public TranslationDictionary createDictionary(@NotNull String value, @NotNull String languageCode, Translatable translatable) {
		Language language = getLanguage(languageCode);
		return createDictionary(value, language, translatable);
	}

	/**
	 * Creates a dictionary entry with requested original language (sets content to auto translatable). This function does NOT save to DB.
	 *
	 * @param value        text to be internationalized
	 * @param languageCode the original language of the text
	 * @return a new TranslationDictionary with entry in original language
	 */
	@Transactional
	public TranslationDictionary createDictionary(@NotNull String value, @NotNull String languageCode) {
		return this.createDictionary(value, languageCode, Translatable.AutoTranslation);
	}

	/**
	 * Gets all languages that can be used for translations
	 *
	 * @return all languages that can be used for translations
	 */
	public List<Language> getLanguages() {
		return languageCacheService.getAllLanguages();
	}

	/**
	 * Gets the languages by code that can be used for translations
	 *
	 * @return a languages by its code
	 * @throws ItemNotFoundException when language with given `languageCode` does not exist
	 */
	public Language getLanguage(String languageCode) {
		return languageCacheService.getLanguage(languageCode)
			.orElseThrow(() -> new ItemNotFoundException(Language.class, languageCode));
	}

	/**
	 * Adds a language that can be used for translations
	 *
	 * @param languageCode a valid language code (2 or 3 chars) from the table
	 * @return language added
	 * @link https://www.loc.gov/standards/iso639-2/php/code_list.php
	 */
	public Language addLocale(@NotBlank String languageCode) {
		return languageService.addLanguage(languageCode);
	}

	public TranslationDictionary getTranslationDictionary(UUID id) {
		return translationDictionaryRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(TranslationDictionary.class, id));
	}

	/**
	 * Updates the dictionary, if the entry in language exists its updated if it doesn't its created.
	 * Saves to DB if there are changes.
	 *
	 * @param id      id of dictionary to update
	 * @param request update with new values
	 * @return updated dictionary
	 */
	@Transactional
	public TranslationDictionary updateTranslationDictionary(UUID id, @Valid UpdateTranslationDictionaryRequest request) {
		TranslationDictionary translationDict = translationDictionaryRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(TranslationDictionary.class, id));
		//try to update the entry
		if (!updateTranslationDictionaryWithItem(request, translationDict))
			this.dictionaryNeedsReTranslation(translationDict, null);
		return translationDictionaryRepository.save(translationDict);
	}

	/**
	 * @return true if there was an update
	 */
	private boolean updateTranslationDictionaryWithItem(UpdateTranslationDictionaryRequest request, TranslationDictionary translationDict) {
		Language language = getLanguage(request.getLanguageCode());

		//try to update the existing item
		var existingItem = translationDict.getItems().stream()
			.filter(item -> item.getLanguage().getCode().contentEquals(request.getLanguageCode()))
			.findFirst();

		if (existingItem.isPresent()) {
			//check if value has changed
			if (existingItem.get().getValue().contentEquals(request.getContent()))
				return false;
			//update the item
			existingItem.get().setValue(request.getContent());
			existingItem.get().setUpdatedAt(LocalDateTime.now());
		} else {
			//add a new one
			var newItem = new DictionaryItem();
			newItem.setLanguage(language);
			newItem.setValue(request.getContent());
			newItem.setTranslationDictionary(translationDict);
			translationDict.getItems().add(newItem);
		}
		return true;
	}

	/**
	 * Updates the dictionary, if the entry in language exists its updated if it doesn't its created.
	 * Saves to DB if there are changes.
	 *
	 * @param id      id of dictionary to update
	 * @param request update with new values
	 * @return updated dictionary
	 */
	@Transactional
	public TranslationDictionary updateTranslationsInDictionary(UUID id, @Valid UpdateTranslationsInDictionaryRequest request) {
		//check if there are duplicates
		Set<String> uniqueLangSet = new HashSet<>();
		for (var item : request.getTranslations()) {
			if (!uniqueLangSet.add(item.getLanguageCode()))
				throw new InvalidParametersException("Duplicate language code: " + item.getLanguageCode());
		}

		TranslationDictionary translationDict = translationDictionaryRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(TranslationDictionary.class, id));

		boolean anyChanged = false;
		//try to update the items
		for (UpdateTranslationDictionaryRequest item : request.getTranslations()) {
			if (updateTranslationDictionaryWithItem(item, translationDict))
				anyChanged = true;
		}
		if (!anyChanged)
			return translationDict;

		//check if there are all translatable languages but the original (that means everything is translated)
		var languageCodes = this.getLanguages().stream().map(Language::getCode).collect(Collectors.toSet());
		if (uniqueLangSet.size() == languageCodes.size() - 1)
			translationDict.setNeedsReTranslation(false);
		else
			this.dictionaryNeedsReTranslation(translationDict, null);
		return translationDictionaryRepository.save(translationDict);
	}

	@Transactional
	public TranslationDictionary updateOriginalTranslation(
		TranslationDictionary dictionary,
		String content
	) {
		Language originalLanguage = dictionary.getOriginalLanguage();

		DictionaryItem originalItem = dictionary.getItems().stream()
			.filter(item ->
				item.getLanguage().getCode().equals(originalLanguage.getCode())
			)
			.findFirst()
			.orElseThrow(() -> new ItemNotFoundException(
				DictionaryItem.class, originalLanguage.getCode()
			));

		originalItem.setValue(content);
		this.dictionaryNeedsReTranslation(dictionary, true);

		return translationDictionaryRepository.save(dictionary);
	}

	/**
	 * Removes a language, only a language that is not used can be removed
	 *
	 * @param languageCode language to remove
	 */
	public void removeLocale(@NotBlank String languageCode) {
		languageService.deleteById(languageCode);
	}

	/**
	 * Fetches a slice of TranslationDictionary list that need updating.
	 * This method uses cached resources.
	 *
	 * @param cursor  the cursor that points to the next slice
	 * @param size    size of the slice to return
	 * @param filters limit the search to matching filters. autoTranslatable field (false for code blocks, true for others)
	 */
	public Slice<@NonNull TranslationDictionary> GetTranslationDictionariesThatNeedTranslating(String cursor, int size, GetUntranslatedDictionariesFilters filters) {
		return this.GetTranslationDictionariesThatNeedTranslating(cursor, size, filters, Specification.unrestricted());
	}

	/**
	 * Fetches a slice of TranslationDictionary list that need updating.
	 *
	 * @param cursor  the cursor that points to the next slice
	 * @param size    size of the slice to return
	 * @param filters limit search to matching filters
	 */
	private Slice<@NonNull TranslationDictionary> GetTranslationDictionariesThatNeedTranslating(String cursor, int size, GetUntranslatedDictionariesFilters filters, Specification<TranslationDictionary> specFilters) {
		Specification<TranslationDictionary> specification = Specification.allOf(
			specFromFilters(filters),
			TranslationDictionarySpecification.needsReTranslation(),
			specFilters); //apply all other filters

		return GetTranslationDictionariesMatching(cursor, size, specification);
	}

	private Specification<TranslationDictionary> specFromFilters(GetUntranslatedDictionariesFilters filters) {
		var translationDictionarySpecification = new TranslationDictionarySpecification();
		Specification<TranslationDictionary> specification = Specification.allOf(
			translationDictionarySpecification.orderByCreatedAtDateId(Sort.Direction.DESC),
			TranslationDictionarySpecification.autoTranslatable(filters.getAutoTranslatable())
		);
		if (filters.getCourseId() != null) {
			var dictionaryUuidsInCourse = courseDictionaryCacheService.getDictionaryUuidsInCourse(filters.getCourseId());
			specification = translationDictionarySpecification.hasIdIn(dictionaryUuidsInCourse);
		}
		if (filters.getUpdatedAfter() != null)
			specification = specification.and(translationDictionarySpecification.updateAfter(filters.getUpdatedAfter().atStartOfDay()));
		if (filters.getCreatedAfter() != null)
			specification = specification.and(translationDictionarySpecification.updateAfter(filters.getCreatedAfter().atStartOfDay()));
		return specification;
	}

	private Slice<@NonNull TranslationDictionary> GetTranslationDictionariesMatching(String cursor, int size, Specification<TranslationDictionary> specFilters) {
		if (size > maxSizeFetchable) size = maxSizeFetchable;
		if (size < 1) size = 1;

		var translationDictionarySpecification = new TranslationDictionarySpecification();
		if (cursor != null && !cursor.isBlank()) {
			TranslationDictionaryCursor decoded = CursorUtil.decode(TranslationDictionaryCursor.class, cursor);
			specFilters = specFilters.and(translationDictionarySpecification.isBefore(decoded.createdAt(), decoded.id()));
		}
		return translationDictionaryRepository.findAllSliced(specFilters, PageRequest.of(0, size));
	}


	/**
	 * Fetches all dictionary ids in a course and limits them to `size` in responses
	 * This method uses cached resources.
	 *
	 * @param courseId id of a course to get dictionary ids for.
	 * @param cursor   the next item to retrieve
	 * @param size     the amount of items to retrieve in range [1, maxSizeReturnable]
	 * @return a list of dictionary ids used in course
	 */
	public List<UUID> getCourseDictionaryIds(UUID courseId, String cursor, int size) {
		if (size <= 0) size = 1;
		if (size > maxSizeReturnable) size = maxSizeReturnable;
		int cursorPos = 0;
		if (StringUtils.isNotBlank(cursor))
			cursorPos = Integer.parseInt(cursor);

		List<UUID> itemIds = courseDictionaryCacheService.getDictionaryUuidsInCourse(courseId);
		return itemIds.stream().skip(cursorPos).limit(size).toList();
	}


	/**
	 * Fetches a slice of TranslationDictionary list that need updating.
	 *
	 * @param cursor       the cursor that points to the next slice
	 * @param size         size of the slice to return
	 * @param filters      limit search to matching filters
	 * @param languageCode language that is missing / not up to date
	 */
	public Slice<@NonNull TranslationDictionary> GetTranslationDictionariesThatNeedTranslatingInto(String cursor, int size, GetUntranslatedDictionariesFilters filters, @NotEmpty String languageCode) {
		var lang = languageCacheService.getLanguage(languageCode)
			.orElseThrow(() -> new ItemNotFoundException(Language.class, languageCode));

		return GetTranslationDictionariesMatching(cursor, size,
			specFromFilters(filters)
				.and(TranslationDictionarySpecification.missingLanguage(languageCode)));
	}
}
