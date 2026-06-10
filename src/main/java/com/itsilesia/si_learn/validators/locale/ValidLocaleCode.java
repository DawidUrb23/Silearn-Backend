package com.itsilesia.si_learn.validators.locale;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidLocaleCodeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLocaleCode {
	String message() default "Invalid locale code";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
