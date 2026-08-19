package com.itsilesia.si_learn.entities;

import com.itsilesia.si_learn.entities.enums.Translatable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

/// Internationalization dictionary that stores the original language, its content and its translations
@Entity(name = "translation_dictionary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranslationDictionary extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_code", nullable = false)
	private Language originalLanguage;

	/**
	 * This defines whether this entry can be translated and how (not translatable for code snippets)
	 */
	@ColumnDefault("2")
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "translatable", nullable = false)
	private Translatable translatable = Translatable.AutoTranslation;

	/**
	 * This defines whether this entry needs to be translated (automatic or manual, code blocks should not be translated)
	 */
	@ColumnDefault("true")
	@Column(name = "needs_re_translation", nullable = false)
	private boolean needsReTranslation = true;

	@OneToMany(mappedBy = DictionaryItem_.TRANSLATION_DICTIONARY, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<DictionaryItem> items = new ArrayList<>();
}
