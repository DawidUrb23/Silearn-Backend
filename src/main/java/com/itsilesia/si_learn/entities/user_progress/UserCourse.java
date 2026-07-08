package com.itsilesia.si_learn.entities.user_progress;


import com.itsilesia.si_learn.dtos.response.TwoIntsProgress;
import com.itsilesia.si_learn.entities.compositeIds.UserCourseId;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "user_courses")
@IdClass(UserCourseId.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCourse extends StartFinishForUserProgress {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	@OneToMany(mappedBy = UserModule_.USER_COURSE, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserModule> userModules = new ArrayList<>();

	public TwoIntsProgress progress() {
		return new TwoIntsProgress(
			course.getModules().size(),
			(int) userModules.stream().filter(
				it -> it.progressStatus() == ProgressStatus.FINISHED
			).count()
		);
	}

}
