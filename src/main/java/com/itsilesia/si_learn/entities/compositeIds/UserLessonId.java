package com.itsilesia.si_learn.entities.compositeIds;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLessonId implements Serializable {
	private UUID user;
	private UUID lesson;
}
