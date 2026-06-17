package com.itsilesia.si_learn.entities.courses;

import com.itsilesia.si_learn.entities.BaseEntity;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.entities.enums.ContentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "task_content")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskContent extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id", nullable = false)
	private Task task;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	//Dictionary is Unique for each instance
	@JoinColumn(name = "content_id", nullable = false)
	private TranslationDictionary content;

	@Column(nullable = false)
	private int index;

	@Column(nullable = false, length = 5)
	@Enumerated(EnumType.STRING)
	private ContentType type = ContentType.TXT;
}
