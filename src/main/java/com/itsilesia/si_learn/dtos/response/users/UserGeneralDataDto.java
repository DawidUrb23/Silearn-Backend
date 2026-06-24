package com.itsilesia.si_learn.dtos.response.users;

import com.itsilesia.si_learn.entities.users.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGeneralDataDto {
	private UUID id;
	private String name;
	private String surname;

	static public UserGeneralDataDto fromEntity(@NotNull User user) {
		return new UserGeneralDataDto(
			user.getId(),
			user.getName(),
			user.getSurname()
		);
	}
}
