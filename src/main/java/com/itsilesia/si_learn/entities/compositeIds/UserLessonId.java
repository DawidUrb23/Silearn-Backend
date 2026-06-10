package com.itsilesia.si_learn.entities.compositeIds;


import com.itsilesia.si_learn.entities.courses.Lesson;
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
	private Lesson lesson;
}
