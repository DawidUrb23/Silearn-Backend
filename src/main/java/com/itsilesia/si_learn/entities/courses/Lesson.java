package com.itsilesia.si_learn.entities.courses;

import com.itsilesia.si_learn.entities.BaseEntity;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lesson extends BaseEntity {
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	//Dictionary is Unique for each instance
	@JoinColumn(name = "topic_id", nullable = false)
	private TranslationDictionary topic;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	//Dictionary is Unique for each instance
	@JoinColumn(name = "description_id", nullable = false)
	private TranslationDictionary description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name =  "module_id", nullable = false)
	private Module module;


	@OneToMany(mappedBy = Task_.LESSON, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks;
}

