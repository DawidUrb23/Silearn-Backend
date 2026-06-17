package com.itsilesia.si_learn.entities.compositeIds;

import com.itsilesia.si_learn.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleId implements Serializable {
	private UUID user;
	//this is an Enum (not a class)
	private Role role;
}
