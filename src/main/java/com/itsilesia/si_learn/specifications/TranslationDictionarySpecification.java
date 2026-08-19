package com.itsilesia.si_learn.specifications;

import com.itsilesia.si_learn.entities.*;
import com.itsilesia.si_learn.entities.enums.Translatable;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class TranslationDictionarySpecification extends CommonBaseSpecification<TranslationDictionary> {

	static public Specification<TranslationDictionary> autoTranslatable(Boolean autoTranslatable) {
		return (root, query, criteriaBuilder) -> {
			if (autoTranslatable == null)
				return criteriaBuilder.conjunction();
			return criteriaBuilder.equal(root.get(TranslationDictionary_.TRANSLATABLE),
				autoTranslatable ? Translatable.AutoTranslation : Translatable.ManualTranslation);
		};
	}

	static public Specification<TranslationDictionary> translatable(Translatable translatable) {
		return (root, query, criteriaBuilder) -> {
			if (translatable == null)
				return criteriaBuilder.conjunction();
			return criteriaBuilder.equal(root.get(TranslationDictionary_.TRANSLATABLE), translatable);
		};
	}

	static public Specification<TranslationDictionary> needsReTranslation() {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get(TranslationDictionary_.NEEDS_RE_TRANSLATION), true);
		};
	}

	public static Specification<TranslationDictionary> missingLanguage(String languageCode) {
		return (root, query, criteriaBuilder) -> {
			if (languageCode == null || languageCode.isBlank())
				return criteriaBuilder.conjunction();
			// subquery for DictionaryItem
			Subquery<Integer> subquery = query.subquery(Integer.class);
			Root<DictionaryItem> subqueryRoot = subquery.from(DictionaryItem.class);
			// SELECT language FROM dictionary_items WHERE dictionary_id = root.id AND language_code = languageCode
			subquery.select(subqueryRoot.get(DictionaryItem_.LANGUAGE))
				.where(
					criteriaBuilder.equal(subqueryRoot.get(DictionaryItem_.TRANSLATION_DICTIONARY), root),
					criteriaBuilder.equal(subqueryRoot.get(DictionaryItem_.LANGUAGE).get(Language_.CODE), languageCode)
				);
			// WHERE NOT EXISTS (...)
			return criteriaBuilder.not(criteriaBuilder.exists(subquery));
		};
	}
}
