package com.itsilesia.si_learn.entities.courses;

import com.itsilesia.si_learn.entities.BaseEntity;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.entities.abstractions.IndexedEntity;
import com.itsilesia.si_learn.entities.user_progress.UserModule;
import com.itsilesia.si_learn.entities.user_progress.UserModule_;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "modules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Module extends BaseEntity implements IndexedEntity {
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	//Dictionary is Unique for each instance
	@JoinColumn(name = "title_id", nullable = false)
	private TranslationDictionary title;

	@Column(nullable = false)
	private int index;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;


	@OneToMany(mappedBy = Lesson_.MODULE, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Lesson> lessons = new ArrayList<>();


	@OneToMany(mappedBy = UserModule_.MODULE, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserModule> userModules = new ArrayList<>();
}
