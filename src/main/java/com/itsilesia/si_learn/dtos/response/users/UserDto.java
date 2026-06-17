package com.itsilesia.si_learn.dtos.response.users;

import com.itsilesia.si_learn.entities.enums.Role;
import com.itsilesia.si_learn.entities.users.User;
import com.itsilesia.si_learn.entities.users.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private UUID id;
	private String email;
	private String languageCode;
	private List<Role> roles;

	static public UserDto fromEntity(@NotNull User user) {
		return new UserDto(
			user.getId(),
			user.getEmail(),
			user.getLanguage() == null ? null : user.getLanguage().getCode(),
			user.getRoles().stream().map(UserRole::getRole).toList()
		);
	}
}
