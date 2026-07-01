package com.itsilesia.si_learn.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

@Entity(name = "languages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Language {
	@Id
	@Column(name = "code", length = 10, unique = true, nullable = false)
	private String code;

	//we use ISO 639 alpha-2 (that means 2 letters make a language code)
	// When a language has both an alpha-2 code and an alpha-3 code, the alpha-2 code must be used.
	//https://www.loc.gov/standards/iso639-2/php/code_list.php
	public Locale getLocale() {
		return Locale.forLanguageTag(code);
	}
}
