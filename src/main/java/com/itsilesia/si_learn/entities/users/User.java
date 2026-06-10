package com.itsilesia.si_learn.entities.users;

import com.itsilesia.si_learn.entities.BaseEntity;
import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.courses.Course_;
import com.itsilesia.si_learn.entities.user_progress.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@OneToMany(mappedBy = UserRole_.USER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRole> roles;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_code", nullable = true)
	private Language language;


	@OneToMany(mappedBy = Course_.AUTHOR)
	private List<Course> authoredCourses = new ArrayList<>();


	@OneToMany(mappedBy = UserCourse_.USER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserCourse> userCourses = new ArrayList<>();

	@OneToMany(mappedBy = UserModule_.USER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserModule> userModules = new ArrayList<>();

	@OneToMany(mappedBy = UserLesson_.USER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserLesson> userLessons = new ArrayList<>();

	@OneToMany(mappedBy = UserTask_.USER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserTask> userTasks = new ArrayList<>();
}
