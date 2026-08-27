package com.itsilesia.si_learn.entities.courses;

import com.itsilesia.si_learn.entities.BaseEntity;
import com.itsilesia.si_learn.entities.abstractions.CourseAccessNode;
import com.itsilesia.si_learn.entities.abstractions.IndexedEntity;
import com.itsilesia.si_learn.entities.user_progress.UserCodeExecution;
import com.itsilesia.si_learn.entities.user_progress.UserCodeExecution_;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import com.itsilesia.si_learn.entities.user_progress.UserTask_;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity implements IndexedEntity, CourseAccessNode {
	@Column(nullable = false)
	private int index;

	/**
	 * Value is [0, 1]
	 */
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

	@OneToMany(mappedBy = UserTask_.TASK, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserTask> userTasks = new ArrayList<>();

	@OneToMany(mappedBy = UserCodeExecution_.TASK, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private List<UserCodeExecution> userCodeExecutions = new ArrayList<>();

	@Override
	public Collection<? extends CourseAccessNode> getSiblings() {
		return this.getLesson().getTasks();
	}

	@Override
	public CourseAccessNode getParentNode() {
		return this.getLesson();
	}

	@Override
	public Course getCourse() {
		return this.getParentNode().getCourse();
	}
}
