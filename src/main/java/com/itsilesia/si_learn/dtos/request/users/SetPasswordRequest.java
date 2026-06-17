package com.itsilesia.si_learn.dtos.request.users;

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
	@Size(min = 8, max = 1024)
	private String newPassword;
}
