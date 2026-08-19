package com.itsilesia.si_learn.dtos.request.users;

import com.itsilesia.si_learn.config.ValidationConstants;
import com.itsilesia.si_learn.validators.locale.ValidLocaleCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Locale;

@Data
public class RegisterRequest {
	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Size(min = ValidationConstants.passwordLenMin, max = ValidationConstants.passwordLenMax)
	private String password;

	@ValidLocaleCode
	private Locale languageCode;

	@NotBlank
	@Size(max = ValidationConstants.nameLenMax)
	private String name;

	@NotBlank
	@Size(max = ValidationConstants.surnameLenMax)
	private String surname;

	private boolean teacherRole = false;
}
