package com.itsilesia.si_learn.dtos.request.dictionary;

import com.itsilesia.si_learn.config.ValidationConstants;
import com.itsilesia.si_learn.validators.locale.ValidLocaleCode;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTranslationDictionaryRequest {
	@ValidLocaleCode
	private Locale languageCode;

	@Size(max = ValidationConstants.translationDictItemContentMaxLen)
	private String content;
}
