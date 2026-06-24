package com.itsilesia.si_learn.specifications;

import com.itsilesia.si_learn.entities.DictionaryItem;
import com.itsilesia.si_learn.entities.DictionaryItem_;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.entities.TranslationDictionary_;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.courses.Course_;
import com.itsilesia.si_learn.entities.enums.DifficultyLevel;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.entities.users.User_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CourseSpecification {
	public static Specification<Course> hasDifficulty(DifficultyLevel difficulty) {
		return (root, query, criteriaBuilder) -> {
			if (difficulty == null) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.equal(root.get(Course_.DIFFICULTY), difficulty);
		};
	}

	public static Specification<Course> hasProgramingLanguage(ProgramingLanguage programingLanguage) {
		return (root, query, criteriaBuilder) -> {
			if (programingLanguage == null) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.equal(root.get(Course_.PROGRAMING_LANGUAGE), programingLanguage);
		};
	}

	public static Specification<Course> hasAuthor(UUID authorId) {
		return (root, query, criteriaBuilder) -> {
			if (authorId == null) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.equal(root.get(Course_.AUTHOR).get(User_.ID), authorId);
		};
	}

	/**
	 * Checks if title in ANY language contains the sequence
	 * @param name - part of title to match
	 */
	public static Specification<Course> anyTitleContains(String name) {
		return (root, query, criteriaBuilder) -> {
			if (name == null || name.isBlank()) {
				return criteriaBuilder.conjunction();
			}

			Join<Course, TranslationDictionary> titleJoin = root.join(Course_.TITLE);
			Join<TranslationDictionary, DictionaryItem> itemsJoin = titleJoin.join(TranslationDictionary_.ITEMS);

			return criteriaBuilder.like(
				criteriaBuilder.lower(itemsJoin.get(DictionaryItem_.VALUE)),
				"%" + name.toLowerCase() + "%"
			);
		};
	}
}
