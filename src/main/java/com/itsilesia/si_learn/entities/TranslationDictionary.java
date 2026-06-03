package com.itsilesia.si_learn.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	@OneToMany(mappedBy = DictionaryItem_.TRANSLATION_DICTIONARY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DictionaryItem> items = new ArrayList<>();
}
