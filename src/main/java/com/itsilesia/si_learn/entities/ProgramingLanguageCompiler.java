package com.itsilesia.si_learn.entities;

import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "programing_languages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramingLanguageCompiler extends BaseEntity {
	@Enumerated(EnumType.STRING)
	@Column(name = "language", length = 30, nullable = false, unique = true)
	private ProgramingLanguage language;

	@Column(length = 30, nullable = true)
	private String compiler;

	@Column(length = 30, nullable = true, name = "compiler_name_prefix")
	private String compilerNamePrefix;
}
