package com.itsilesia.si_learn.entities.courses;

import com.itsilesia.si_learn.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity {
	@Column(nullable = false)
	private int index;

	@Column(name = "passing_score", nullable = false)
	private float passingScore;

	@Column(name = "starting_code", nullable = true, columnDefinition = "TEXT")
	private String startingCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lesson_id", nullable = false)
	private Lesson lesson;


	@OneToMany(mappedBy = TaskContent_.TASK, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TaskContent> taskContents = new ArrayList<>();

	@OneToMany(mappedBy = TestScenario_.TASK, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TestScenario> testScenarios = new ArrayList<>();
}
