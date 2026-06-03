package com.itsilesia.si_learn.services.users;

import com.itsilesia.si_learn.entities.enums.Role;
import com.itsilesia.si_learn.entities.users.User;
import com.itsilesia.si_learn.entities.users.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

	public void addRoleToUser(User user, Role role) {
		var roles = user.getRoles();
		if (roles.stream()
			.anyMatch(r -> r.getRole().equals(role)))
			return;
		roles.add(new UserRole(user, role));
		user.setRoles(roles);
	}
}
