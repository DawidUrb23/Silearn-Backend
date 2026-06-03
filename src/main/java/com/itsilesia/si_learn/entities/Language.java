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

	public Locale getLocale() {
		return Locale.forLanguageTag(code);
	}
}
