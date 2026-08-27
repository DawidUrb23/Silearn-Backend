package com.itsilesia.si_learn.services.user_progress;

import com.itsilesia.si_learn.config.ValidationConstants;
import com.itsilesia.si_learn.entities.user_progress.UserCodeExecution;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.user_progress.UserCodeExecutionRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserTaskRepository;
import com.itsilesia.si_learn.specifications.UserCodeExecutionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAutoTestCheckService {
	private final UserCodeExecutionRepository userCodeExecutionRepository;
	private final UserTaskRepository userTaskRepository;
	private final UserCourseHelperService userCourseHelperService;

	@Transactional
	public void gradeUserTaskByCompilation(UUID compilationId) {
		var specification = UserCodeExecutionSpecification.hasCompilationId(compilationId);
		var userCodeExecution = userCodeExecutionRepository.findOne(specification)
			.orElseThrow(() -> new ItemNotFoundException(UserCodeExecution.class, compilationId));

		UUID groupId = userCodeExecution.getGroupId();
		//this auto check is still not complete
		if (userCodeExecutionRepository.exists(UserCodeExecutionSpecification.anyPendingInGroupId(groupId)))
			return;
		var task = userCodeExecution.getTask();
		var userTask = userCodeExecution.getUserTask();
		float resultMultiplier = (float) ValidationConstants.gradeMaxScore;
		var groupedResults = userCodeExecutionRepository.findAll(UserCodeExecutionSpecification.hasGroupId(groupId));
		int gradedPositive = 0;
		for (var item : groupedResults) {
			Integer exitCode = item.getExitCode();
			if (exitCode != null && exitCode.intValue() != 0)
				continue;
			var testScenario = item.getTestScenario();
			var compilerOutput = item.getCompilerOutput();
			if (this.compareOutputs(compilerOutput, testScenario.getExpectedOutput())) {
				gradedPositive++;
			}
		}
		var userFinalScore = resultMultiplier * (float) gradedPositive / (float) groupedResults.size();
		userTask.setGradedAt(LocalDateTime.now());
		userTask.setTaskScore(userFinalScore);
		if (userFinalScore >= task.getPassingScore())
			userTask.setPassed(true);
		userCourseHelperService.propagateUpdateProgress(userTask);
		userTaskRepository.save(userTask);
	}

	private boolean compareOutputs(String actual, String expected) {
		if (actual == null || expected == null) return false;

		String normActual = actual.replace("\r\n", "\n")
			.lines()
			.map(String::strip)
			.collect(Collectors.joining())
			.strip();


		String normExpected = expected.replace("\r\n", "\n")
			.lines()
			.map(String::strip)
			.collect(Collectors.joining())
			.strip();

		return normActual.equals(normExpected);
	}
}