package com.itsilesia.si_learn.dtos.request.users;

import com.itsilesia.si_learn.config.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Size(max = ValidationConstants.passwordLenMax)
	private String password;
}
