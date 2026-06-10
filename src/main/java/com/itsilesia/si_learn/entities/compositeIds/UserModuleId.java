package com.itsilesia.si_learn.entities.compositeIds;


import com.itsilesia.si_learn.entities.courses.Module;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModuleId implements Serializable {
	private UUID user;
	private Module module;
}
