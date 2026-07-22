package com.itsilesia.si_learn.entities.courses;

import com.itsilesia.si_learn.entities.BaseEntity;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.entities.abstractions.CourseAccessNode;
import com.itsilesia.si_learn.entities.abstractions.IndexedEntity;
import com.itsilesia.si_learn.entities.user_progress.UserLesson;
import com.itsilesia.si_learn.entities.user_progress.UserLesson_;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lesson extends BaseEntity implements IndexedEntity, CourseAccessNode {
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	//Dictionary is Unique for each instance
	@JoinColumn(name = "topic_id", nullable = false)
	private TranslationDictionary topic;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	//Dictionary is Unique for each instance
	@JoinColumn(name = "description_id", nullable = false)
	private TranslationDictionary description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "module_id", nullable = false)
	private Module module;

	@Column(nullable = false)
	private int index;

	@OneToMany(mappedBy = Task_.LESSON, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks;


	@OneToMany(mappedBy = UserLesson_.LESSON, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserLesson> userLessons = new ArrayList<>();

	@Override
	public Collection<? extends CourseAccessNode> getSiblings() {
		return this.getModule().getLessons();
	}

	@Override
	public CourseAccessNode getParentNode() {
		return this.getModule();
	}

	@Override
	public Course getCourse() {
		return this.getParentNode().getCourse();
	}
}
