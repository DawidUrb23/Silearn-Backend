package com.itsilesia.si_learn.entities.user_progress;


import com.itsilesia.si_learn.entities.compositeIds.UserLessonId;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.enums.ProgressStatusHelper;
import com.itsilesia.si_learn.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "user_lessons")
@IdClass(UserLessonId.class)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserLesson {

	@Column(name = "start_date", nullable = false, columnDefinition = "Date")
	private LocalDateTime startDate;

	@Column(name = "end_date", nullable = true, columnDefinition = "Date")
	private LocalDateTime endDate;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lesson_id", nullable = false)
	private Lesson lesson;


	public ProgressStatus progressStatus() {
		return ProgressStatusHelper.getStatus(startDate, endDate);
	}
}
