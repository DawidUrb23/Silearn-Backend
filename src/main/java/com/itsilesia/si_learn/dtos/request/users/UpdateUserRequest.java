package com.itsilesia.si_learn.dtos.request.users;

import com.itsilesia.si_learn.config.ValidationConstants;
import com.itsilesia.si_learn.validators.locale.ValidLocaleCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
	@Email
	private String email;

	@ValidLocaleCode
	private Locale languageCode;

	@Size(max = ValidationConstants.nameLenMax)
	private String name;

	@Size(max = ValidationConstants.surnameLenMax)
	private String surname;

	private Boolean becomeTeacher = false;
}
