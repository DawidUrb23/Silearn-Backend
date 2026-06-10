package com.itsilesia.si_learn.entities.user_progress;


import com.itsilesia.si_learn.entities.compositeIds.UserCourseId;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.enums.ProgressStatusHelper;
import com.itsilesia.si_learn.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "user_courses")
@IdClass(UserCourseId.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCourse {

	@Column(name = "start_date", nullable = false, columnDefinition = "Date")
	private LocalDateTime startDate;

	@Column(name = "end_date", nullable = true, columnDefinition = "Date")
	private LocalDateTime endDate;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;


	public ProgressStatus progressStatus() {
		return ProgressStatusHelper.getStatus(startDate, endDate);
	}

	public float progress() {
		if (!course.getModules().isEmpty()) {
			return (float) 1 / course.getModules().size();
		}
		return 0;
	}

}
