package com.itsilesia.si_learn.entities.users;

import com.itsilesia.si_learn.entities.BaseEntity;
import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.courses.Course_;
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
	private String email;

	private String password;

	@OneToMany(mappedBy = UserRole.user_, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRole> roles;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_code", nullable = true)
	private Language language;


	@OneToMany(mappedBy = Course_.AUTHOR)
	private List<Course> authoredCourses = new ArrayList<>();
}
