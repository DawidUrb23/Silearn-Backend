package com.itsilesia.si_learn.specifications;

import com.itsilesia.si_learn.entities.*;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.courses.Course_;
import com.itsilesia.si_learn.entities.enums.DifficultyLevel;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.entities.enums.VisibilityStatus;
import com.itsilesia.si_learn.entities.users.User_;
import jakarta.persistence.criteria.*;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.UUID;

public class CourseSpecification extends CommonBaseSpecification<Course> {
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
	 * Checks if the query matches:
	 * - course title,
	 * - course description,
	 * - author's name,
	 * - author's surname,
	 * - author's full name.
	 *
	 * Title and description are searched in the user's chosen language,
	 * with fallback to the original language.
	 *
	 * @param searchQuery text to search for
	 */
	public static Specification<Course> matchesQuery(String searchQuery) {
		return (root, criteriaQuery, criteriaBuilder) -> {
			if (searchQuery == null || searchQuery.isBlank())
				return criteriaBuilder.conjunction();

			criteriaQuery.distinct(true);

			String userLanguage = LocaleContextHolder.getLocale().getLanguage();
			String pattern = "%" + searchQuery.trim().toLowerCase() + "%";

			Join<Course, TranslationDictionary> titleJoin =
				root.join(Course_.TITLE);

			Join<Course, TranslationDictionary> descriptionJoin =
				root.join(Course_.DESCRIPTION);

			Predicate titleMatches = dictionaryContains(
				criteriaQuery,
				criteriaBuilder,
				titleJoin,
				userLanguage,
				pattern
			);

			Predicate descriptionMatches = dictionaryContains(
				criteriaQuery,
				criteriaBuilder,
				descriptionJoin,
				userLanguage,
				pattern
			);

			Predicate authorNameMatches = criteriaBuilder.like(
				criteriaBuilder.lower(
					root.get(Course_.AUTHOR).get(User_.NAME)
				),
				pattern
			);

			Predicate authorSurnameMatches = criteriaBuilder.like(
				criteriaBuilder.lower(
					root.get(Course_.AUTHOR).get(User_.SURNAME)
				),
				pattern
			);

			Predicate authorFullNameMatches = criteriaBuilder.like(
				criteriaBuilder.lower(
					criteriaBuilder.concat(
						criteriaBuilder.concat(
							root.get(Course_.AUTHOR).get(User_.NAME),
							" "
						),
						root.get(Course_.AUTHOR).get(User_.SURNAME)
					)
				),
				pattern
			);

			return criteriaBuilder.or(
				titleMatches,
				descriptionMatches,
				authorNameMatches,
				authorSurnameMatches,
				authorFullNameMatches
			);
		};
	}

	private static Predicate dictionaryContains(
		CriteriaQuery<?> criteriaQuery,
		CriteriaBuilder criteriaBuilder,
		Join<Course, TranslationDictionary> dictionaryJoin,
		String userLanguage,
		String pattern
	) {
		Join<TranslationDictionary, DictionaryItem> itemsJoin =
			dictionaryJoin.join(TranslationDictionary_.ITEMS);

		Subquery<Integer> userLanguageExistsSubquery =
			criteriaQuery.subquery(Integer.class);

		Root<DictionaryItem> subqueryRoot =
			userLanguageExistsSubquery.from(DictionaryItem.class);

		userLanguageExistsSubquery
			.where(
				criteriaBuilder.equal(
					subqueryRoot.get(DictionaryItem_.TRANSLATION_DICTIONARY),
					dictionaryJoin
				),
				criteriaBuilder.equal(
					subqueryRoot
						.get(DictionaryItem_.LANGUAGE)
						.get(Language_.CODE),
					userLanguage
				)
			);

		Predicate userLanguageMatches = criteriaBuilder.and(
			criteriaBuilder.equal(
				itemsJoin
					.get(DictionaryItem_.LANGUAGE)
					.get(Language_.CODE),
				userLanguage
			),
			criteriaBuilder.like(
				criteriaBuilder.lower(
					itemsJoin.get(DictionaryItem_.VALUE)
				),
				pattern
			)
		);

		Predicate originalLanguageMatches = criteriaBuilder.and(
			criteriaBuilder.equal(
				itemsJoin
					.get(DictionaryItem_.LANGUAGE)
					.get(Language_.CODE),
				dictionaryJoin
					.get(TranslationDictionary_.ORIGINAL_LANGUAGE)
					.get(Language_.CODE)
			),
			criteriaBuilder.like(
				criteriaBuilder.lower(
					itemsJoin.get(DictionaryItem_.VALUE)
				),
				pattern
			),
			criteriaBuilder.not(
				criteriaBuilder.exists(userLanguageExistsSubquery)
			)
		);

		return criteriaBuilder.or(
			userLanguageMatches,
			originalLanguageMatches
		);
	}

}
