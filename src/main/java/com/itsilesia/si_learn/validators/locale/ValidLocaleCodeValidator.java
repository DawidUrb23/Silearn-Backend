package com.itsilesia.si_learn.validators.locale;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Locale;

public class ValidLocaleCodeValidator implements ConstraintValidator<ValidLocaleCode, String> {

	@Override
	public boolean isValid(String languageCode, ConstraintValidatorContext context) {
		if (languageCode == null || languageCode.isBlank()) {
			return false; // @NotBlank should handle this, but good to be safe
		}
		try {
			// Attempt to create a Locale object from the code
			Locale locale = Locale.forLanguageTag(languageCode);
			// Check if the locale is "undetermined" or "unknown" which can happen for invalid tags
			return !locale.toLanguageTag().equals("und") && !locale.toLanguageTag().isEmpty();
		} catch (Exception e) {
			return false;
		}
	}
}
