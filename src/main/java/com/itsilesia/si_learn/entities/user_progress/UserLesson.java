package com.itsilesia.si_learn.entities.user_progress;


import com.itsilesia.si_learn.dtos.response.TwoIntsProgress;
import com.itsilesia.si_learn.entities.compositeIds.UserLessonId;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "user_lessons")
@IdClass(UserLessonId.class)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserLesson extends StartFinishForUserProgress {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lesson_id", nullable = false)
	private Lesson lesson;


	//	Handles the actual writing/inserting of the module_id column
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "module_id", nullable = false)
	private Module module;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({  //important order matters, otherwise those 2 columns are swapped (you have to check in DB the order)
		@JoinColumn(name = "module_id", insertable = false, updatable = false), //this part already exists (do not make it)
		@JoinColumn(name = "user_id", insertable = false, updatable = false)
	})
	private UserModule userModule;

	@OneToMany(mappedBy = UserTask_.USER_LESSON, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserTask> userTasks = new ArrayList<>();


	public TwoIntsProgress progress() {
		return new TwoIntsProgress(
			lesson.getTasks().size(),
			(int) userTasks.stream().filter(
				it -> it.progressStatus() == ProgressStatus.FINISHED
			).count()
		);
	}
}
