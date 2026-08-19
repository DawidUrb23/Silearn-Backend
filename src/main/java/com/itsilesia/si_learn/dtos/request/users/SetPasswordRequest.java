package com.itsilesia.si_learn.dtos.request.users;

import com.itsilesia.si_learn.config.ValidationConstants;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetPasswordRequest {
	@NotEmpty
	@Size(min = ValidationConstants.passwordLenMin, max = ValidationConstants.passwordLenMax)
	private String newPassword;
}
