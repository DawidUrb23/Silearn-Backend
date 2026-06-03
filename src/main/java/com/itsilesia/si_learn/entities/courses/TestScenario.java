package com.itsilesia.si_learn.entities.courses;

import com.itsilesia.si_learn.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "test_scenarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestScenario extends BaseEntity {
	@Column(name = "input", columnDefinition = "TEXT", nullable = false)
	private String input;

	@Column(name = "output", columnDefinition = "TEXT", nullable = false)
	private String expectedOutput;

	@Column(name = "timeout_sec", nullable = false)
	private float timeoutSec = 5;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id", nullable = false)
	private Task task;
}
