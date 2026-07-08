package com.itsilesia.si_learn.entities.user_progress;


import com.itsilesia.si_learn.dtos.response.TwoIntsProgress;
import com.itsilesia.si_learn.entities.compositeIds.UserModuleId;
import com.itsilesia.si_learn.entities.courses.Course;
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

@Entity(name = "user_modules")
@IdClass(UserModuleId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserModule extends StartFinishForUserProgress {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "module_id", nullable = false)
	private Module module;


	//  Handles the actual writing/inserting of the course_id column
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({  //important order matters, otherwise those 2 columns are swapped (you have to check in DB the order)
		@JoinColumn(name = "course_id", insertable = false, updatable = false), //this part already exists (do not make it)
		@JoinColumn(name = "user_id", insertable = false, updatable = false)
	})
	private UserCourse userCourse;

	@OneToMany(mappedBy = UserLesson_.USER_MODULE, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserLesson> userLessons = new ArrayList<>();


	public TwoIntsProgress progress() {
		return new TwoIntsProgress(
			module.getLessons().size(),
			(int) userLessons.stream().filter(
				it -> it.progressStatus() == ProgressStatus.FINISHED
			).count()
		);
	}
}
