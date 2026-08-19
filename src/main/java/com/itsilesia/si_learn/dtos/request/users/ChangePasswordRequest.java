package com.itsilesia.si_learn.dtos.request.users;

import com.itsilesia.si_learn.config.ValidationConstants;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
	@NotNull
	@Size(max = ValidationConstants.passwordLenMax)
	private String oldPassword;
	@NotEmpty
	@Size(min = ValidationConstants.passwordLenMin, max = ValidationConstants.passwordLenMax)
	private String newPassword;
}
