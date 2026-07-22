package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.dictionary.UpdateTranslationDictionaryRequest;
import com.itsilesia.si_learn.entities.DictionaryItem;
import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.entities.courses.TaskContent;
import com.itsilesia.si_learn.exceptions.ItemAlreadyExistsException;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.LanguageRepository;
import com.itsilesia.si_learn.repositories.TranslationDictionaryRepository;
import com.itsilesia.si_learn.repositories.courses.CourseRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranslationService {
	private final LanguageRepository languageRepository;
	private final TranslationDictionaryRepository translationDictionaryRepository;
	private final CourseRepository courseRepository;

	@Transactional
	public TranslationDictionary createDictionary(@NotNull String value, @NotNull Language language) {
		TranslationDictionary dictionary = new TranslationDictionary();
		dictionary.setOriginalLanguage(language);

		DictionaryItem item = new DictionaryItem();
		item.setLanguage(language);
		item.setValue(value);
		item.setTranslationDictionary(dictionary);

		dictionary.getItems().add(item);

		return dictionary;
	}

	@Transactional
	public TranslationDictionary createDictionary(@NotNull String value, @NotNull String languageCode) {
		Language language = languageRepository.findById(languageCode)
			.orElseThrow(() -> new ItemNotFoundException(Language.class, languageCode));
		return createDictionary(value, language);
	}

	public List<Language> getLanguages() {
		return languageRepository.findAll();
	}

	@Transactional
	public Language addLocale(@NotBlank String languageCode) {
		languageCode = languageCode.toLowerCase();
		if (languageRepository.existsById(languageCode))
			throw new ItemAlreadyExistsException(Language.class, languageCode);

		var lang = new Language(languageCode);
		return languageRepository.save(lang);
	}

	public TranslationDictionary getTranslationDictionary(UUID id) {
		return translationDictionaryRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(TranslationDictionary.class, id));
	}

	@Transactional
	public TranslationDictionary updateTranslationDictionary(UUID id, @Valid UpdateTranslationDictionaryRequest request) {
		TranslationDictionary translationDict = translationDictionaryRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(TranslationDictionary.class, id));
		Language language = languageRepository.findById(request.getLanguageCode())
			.orElseThrow(() -> new ItemNotFoundException(Language.class, request.getLanguageCode()));

		//try to update the existing item
		var existingItem = translationDict.getItems().stream()
			.filter(item -> item.getLanguage().getCode().equals(request.getLanguageCode()))
			.findFirst();

		if (existingItem.isPresent()) {
			//update the item
			existingItem.get().setValue(request.getContent());
		} else {
			//add a new one
			var newItem = new DictionaryItem();
			newItem.setLanguage(language);
			newItem.setValue(request.getContent());
			newItem.setTranslationDictionary(translationDict);
			translationDict.getItems().add(newItem);
		}

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
				DictionaryItem.class,
				originalLanguage.getCode()
			));

		originalItem.setValue(content);

		return translationDictionaryRepository.save(dictionary);
	}

	@Transactional
	public void removeLocale(@NotBlank String languageCode) {
		languageRepository.deleteById(languageCode);
	}

	public List<TranslationDictionary> getCourseDictionaries(UUID courseId) {
		var course = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));
		List<TranslationDictionary> items = new ArrayList<>();
		items.add(course.getTitle());
		items.add(course.getDescription());
		items.addAll(course.getModules().stream().map(Module::getTitle).toList());
		var lessons = course.getModules().stream().flatMap(module -> module.getLessons().stream()).toList();
		items.addAll(lessons.stream().map(Lesson::getTopic).toList());
		items.addAll(lessons.stream().map(Lesson::getDescription).toList());
		var tasks = lessons.stream().flatMap(lesson -> lesson.getTasks().stream()).toList();
		items.addAll(tasks.stream().flatMap(task -> task.getTaskContents().stream()).map(TaskContent::getContent).toList());

		return items;
	}
}
