package com.itsilesia.si_learn.validators.locale;

import com.itsilesia.si_learn.config.ValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Locale;

public class ValidLocaleCodeValidator implements ConstraintValidator<ValidLocaleCode, Locale> {

	@Override
	public boolean isValid(Locale locale, ConstraintValidatorContext context) {
		if (locale == null || locale.getLanguage().isBlank() || locale.getLanguage().length() > ValidationConstants.languageCodeLenMax)
			return false; // @NotBlank should handle this, but good to be safe
		try {
			var language = locale.getLanguage();
			var langTag = locale.toLanguageTag();
			if (langTag.equalsIgnoreCase("und"))
				return false;
			return language.length() == 2 || language.length() == 3;
		} catch (Exception e) {
			return false;
		}
	}
}
