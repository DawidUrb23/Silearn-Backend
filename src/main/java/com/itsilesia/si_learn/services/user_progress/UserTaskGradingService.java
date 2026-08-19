package com.itsilesia.si_learn.services.user_progress;


import com.itsilesia.si_learn.dtos.request.users.SubmitTaskRequest;
import com.itsilesia.si_learn.entities.courses.TestScenario;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import com.itsilesia.si_learn.repositories.courses.TestScenarioRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTaskGradingService {
	private final TestScenarioRepository testScenarioRepository;

	/**
	 * Grades the user solution automatically
	 * TODO: this should run on @see `TestScenario` to confirm that user solution is correct
	 *
	 * @param userTask user task to work on
	 * @param request  user code
	 */
	@Transactional(readOnly = true)
	public void gradeUserSolutionAutomatically(@NonNull UserTask userTask, @NonNull SubmitTaskRequest request) {
		//check if this task has test scenarios
		if (!testScenarioRepository.existsByTaskId(userTask.getTask().getId()))
			return;
		//run the test scenarios
		var score = this.runTestsOn(userTask.getTask().getTestScenarios(), request.code());

		userTask.setTaskScore(score);
		var scoreToPass = userTask.getTask().getPassingScore();
		//mark that user has passed the task (at least once)
		userTask.setPassed(score >= scoreToPass);

		userTask.setGradedAt(LocalDateTime.now());
	}

	/**
	 * runs tests on user solution
	 *
	 * @param testScenarios test scenarios to run
	 * @param code          user solution
	 * @return score between 0 and 1
	 */
	private float runTestsOn(@NonNull List<@NonNull TestScenario> testScenarios, @NonNull String code) {
		var score = Math.min((float) (code.length()) / 100.0, 1);
		return (float) score;
	}
}
