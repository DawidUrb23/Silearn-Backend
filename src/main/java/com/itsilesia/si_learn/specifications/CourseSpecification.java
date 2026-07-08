package com.itsilesia.si_learn.specifications;

import com.itsilesia.si_learn.entities.*;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.courses.Course_;
import com.itsilesia.si_learn.entities.enums.DifficultyLevel;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.entities.enums.VisibilityStatus;
import com.itsilesia.si_learn.entities.users.User_;
import jakarta.persistence.criteria.Join;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
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

	public static Specification<Course> hasStatus(VisibilityStatus... statuses) {
		return (root, query, criteriaBuilder) -> {
			if (statuses == null || statuses.length == 0 || Arrays.stream(statuses).toList().contains(null)) {
				return criteriaBuilder.conjunction();
			}
			return root.get(Course_.STATUS).in(Arrays.asList(statuses));
		};
	}

	/**
	 * Checks if title in the user chosen language (or original language fallback) contains the sequence.
	 *
	 * @param name - part of title to match
	 */
	public static Specification<Course> anyTitleContains(String name) {
		return (root, query, criteriaBuilder) -> {
			if (name == null || name.isBlank()) {
				return criteriaBuilder.conjunction();
			}

			String userLang = LocaleContextHolder.getLocale().getLanguage();
			String namePattern = "%" + name.toLowerCase() + "%";

			Join<Course, TranslationDictionary> titleJoin = root.join(Course_.TITLE);
			Join<TranslationDictionary, DictionaryItem> itemsJoin = titleJoin.join(TranslationDictionary_.ITEMS);

			// Subquery to check if translation in userLang exists
			jakarta.persistence.criteria.Subquery<Integer> userLangExistsSubquery = query.subquery(Integer.class);
			jakarta.persistence.criteria.Root<DictionaryItem> subqueryRoot = userLangExistsSubquery.from(DictionaryItem.class);
			userLangExistsSubquery.select(criteriaBuilder.literal(1))
				.where(
					criteriaBuilder.equal(subqueryRoot.get(DictionaryItem_.TRANSLATION_DICTIONARY), titleJoin),
					criteriaBuilder.equal(subqueryRoot.get(DictionaryItem_.LANGUAGE).get(Language_.CODE), userLang)
				);

			// Predicate 1: item language matches userLang, and matches the pattern
			jakarta.persistence.criteria.Predicate userLangMatch = criteriaBuilder.and(
				criteriaBuilder.equal(itemsJoin.get(DictionaryItem_.LANGUAGE).get(Language_.CODE), userLang),
				criteriaBuilder.like(criteriaBuilder.lower(itemsJoin.get(DictionaryItem_.VALUE)), namePattern)
			);

			// Predicate 2: item language matches original language, matches the pattern, AND no translation in userLang exists
			jakarta.persistence.criteria.Predicate originalLangMatch = criteriaBuilder.and(
				criteriaBuilder.equal(itemsJoin.get(DictionaryItem_.LANGUAGE).get(Language_.CODE), titleJoin.get(TranslationDictionary_.ORIGINAL_LANGUAGE).get(Language_.CODE)),
				criteriaBuilder.like(criteriaBuilder.lower(itemsJoin.get(DictionaryItem_.VALUE)), namePattern),
				criteriaBuilder.not(criteriaBuilder.exists(userLangExistsSubquery))
			);

			return criteriaBuilder.or(userLangMatch, originalLangMatch);
		};
	}

	public static Specification<Course> isBefore(LocalDateTime createdAt, UUID id) {
		return (root, query, criteriaBuilder) -> {
			if (createdAt == null || id == null) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.or(
				criteriaBuilder.lessThan(root.get(Course_.createdAt), createdAt),
				criteriaBuilder.and(
					criteriaBuilder.equal(root.get(Course_.createdAt), createdAt),
					criteriaBuilder.lessThan(root.get(Course_.id), id)
				)
			);
		};
	}

	public static Specification<Course> orderBy(Sort.Direction orderDirection) {
		return (root, query, criteriaBuilder) -> {
			if (orderDirection.isAscending()) {
				query.orderBy(
					criteriaBuilder.asc(root.get(Course_.CREATED_AT)),
					criteriaBuilder.asc(root.get(Course_.ID))
				);
			} else {
				query.orderBy(
					criteriaBuilder.desc(root.get(Course_.CREATED_AT)),
					criteriaBuilder.desc(root.get(Course_.ID))
				);
			}
			return criteriaBuilder.conjunction();
		};
	}

}
