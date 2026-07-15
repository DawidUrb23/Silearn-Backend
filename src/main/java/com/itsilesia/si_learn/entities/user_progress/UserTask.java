package com.itsilesia.si_learn.entities.user_progress;


import com.itsilesia.si_learn.entities.abstractions.CompoundIdEntityWithUser;
import com.itsilesia.si_learn.entities.compositeIds.UserTaskId;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "user_tasks")
@IdClass(UserTaskId.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserTask implements CompoundIdEntityWithUser {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id", nullable = false)
	private Task task;

	@Column(name = "task_score")
	private Float taskScore;

	private boolean passed;

	@Column(name = "user_solution", nullable = true, columnDefinition = "TEXT")
	private String userSolution;


	// Handles the actual writing/inserting of the lesson_id column
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lesson_id", nullable = false)
	private Lesson lesson;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({  //important order matters, otherwise those 2 columns are swapped (you have to check in DB the order)
		@JoinColumn(name = "lesson_id", insertable = false, updatable = false), //this part already exists (do not make it)
		@JoinColumn(name = "user_id", insertable = false, updatable = false)
	})
	private UserLesson userLesson;


	public ProgressStatus progressStatus() {
		if (passed)
			return ProgressStatus.FINISHED;
		return ProgressStatus.IN_PROGRESS;
	}

	@Override
	public UUID getId() {
		return task.getId();
	}
}
