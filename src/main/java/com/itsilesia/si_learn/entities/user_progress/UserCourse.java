package com.itsilesia.si_learn.entities.user_progress;


import com.itsilesia.si_learn.dtos.response.TwoIntsProgress;
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
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "user_courses")
@IdClass(UserCourseId.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCourse {

	@CreationTimestamp
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
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	@OneToMany(mappedBy = UserModule_.USER_COURSE, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserModule> userModules = new ArrayList<>();

	public ProgressStatus progressStatus() {
		return ProgressStatusHelper.getStatus(startDate, endDate);
	}

	public TwoIntsProgress progress() {
		return new TwoIntsProgress(
			course.getModules().size(),
			(int) userModules.stream().filter(
				it -> it.progressStatus() == ProgressStatus.FINISHED
			).count()
		);
	}

}
