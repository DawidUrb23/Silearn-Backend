package com.itsilesia.si_learn.entities.user_progress;

import com.itsilesia.si_learn.entities.BaseEntity;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.courses.TestScenario;
import com.itsilesia.si_learn.entities.enums.CompilationProgress;
import com.itsilesia.si_learn.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "user_test_scenarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCodeExecution extends BaseEntity {
	@Column(name = "compilation_id", nullable = false, unique = true)
	private UUID compilationId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id", nullable = false)
	private Task task;

	/// if NULL then that means it's manual user input
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_scenario_id", nullable = true)
	private TestScenario testScenario;

	/// when running test scenarios against user solution
	@Column(name = "group_id", nullable = true)
	private UUID groupId;

	@Column(name = "compilation_progress")
	private CompilationProgress compilationProgress;

	/// this is from either the output stream or error stream (when exitCode is not 0)
	@Column(name = "compiler_output", columnDefinition = "TEXT")
	private String compilerOutput;

	@Column(name = "exit_code", nullable = true)
	private Integer exitCode;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({  //important order matters, otherwise those 2 columns are swapped (you have to check in DB the order)
		@JoinColumn(name = "task_id", insertable = false, updatable = false), //this part already exists (do not make it)
		@JoinColumn(name = "user_id", insertable = false, updatable = false)
	})
	private UserTask userTask;
}
