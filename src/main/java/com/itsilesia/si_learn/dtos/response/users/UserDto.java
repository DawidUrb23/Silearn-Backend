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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private String email;
	private String languageCode;
	private List<Role> roles;

	static public UserDto fromEntity(@NotNull User user) {
		return new UserDto(
			user.getEmail(),
			user.getLanguage() == null ? null : user.getLanguage().getCode(),
			user.getRoles().stream().map(UserRole::getRole).toList()
		);
	}
}
