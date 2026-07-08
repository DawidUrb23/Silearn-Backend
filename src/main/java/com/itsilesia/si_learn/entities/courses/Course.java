package com.itsilesia.si_learn.entities.courses;

import com.itsilesia.si_learn.entities.BaseEntity;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.entities.enums.DifficultyLevel;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.entities.enums.VisibilityStatus;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import com.itsilesia.si_learn.entities.user_progress.UserCourse_;
import com.itsilesia.si_learn.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course extends BaseEntity {
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	//Dictionary is Unique for each instance
	@JoinColumn(name = "title_id", nullable = false)
	private TranslationDictionary title;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	//Dictionary is Unique for each instance
	@JoinColumn(name = "description_id", nullable = false)
	private TranslationDictionary description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id", nullable = false)
	private User author;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private VisibilityStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "difficulty", nullable = false, length = 20)
	private DifficultyLevel difficulty;

	@Enumerated(EnumType.STRING)
	@Column(name = "programing_language", nullable = false, length = 40)
	private ProgramingLanguage programingLanguage;

	@Column(name = "force_sequential_tasks", nullable = false)
	private boolean forceSequentialTasks = false;


	@OneToMany(mappedBy = Module_.COURSE, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Module> modules = new ArrayList<>();


	@OneToMany(mappedBy = UserCourse_.COURSE, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserCourse> userCourses = new ArrayList<>();
}
