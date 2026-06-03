package com.itsilesia.si_learn.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "dictionary_items")
@Table(
	name = "dictionary_items",
	uniqueConstraints = {
		// Enforces that a dictionary can only have one value per language
		@UniqueConstraint(name = "uk_dict_item_lang", columnNames = {"dictionary_id", "language_code"})
	}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryItem extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_code", nullable = false, updatable = false)
	private Language language;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String value;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dictionary_id", nullable = false)
	private TranslationDictionary translationDictionary;
}
