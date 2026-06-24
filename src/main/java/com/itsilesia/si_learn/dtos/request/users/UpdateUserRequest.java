package com.itsilesia.si_learn.dtos.request.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
	@Email
	private String email;

	@Size(max = 30)
	private String languageCode;

	private Boolean becomeTeacher = false;
}
