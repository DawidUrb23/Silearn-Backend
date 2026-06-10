package com.itsilesia.si_learn.entities.users;

import com.itsilesia.si_learn.entities.compositeIds.UserRoleId;
import com.itsilesia.si_learn.entities.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user_roles")
@IdClass(UserRoleId.class) // Composite key (user, role)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
	@Id // Part of the composite key
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Id // Part of the composite key
	@Enumerated(EnumType.STRING)
	@Column(name = "role", length = 30, nullable = false)
	private Role role;
}

