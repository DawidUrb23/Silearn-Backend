package com.itsilesia.si_learn.entities.compositeIds;


import com.itsilesia.si_learn.entities.courses.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTaskId implements Serializable {
	private UUID user;
	private Task task;
}
