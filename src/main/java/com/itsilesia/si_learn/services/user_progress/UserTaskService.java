package com.itsilesia.si_learn.services.user_progress;

import com.itsilesia.si_learn.dtos.request.compiler.SubmitExecuteTaskRequest;
import com.itsilesia.si_learn.dtos.response.CompilationResponse;
import com.itsilesia.si_learn.dtos.response.user_progress.SubmitTestTaskRequest;
import com.itsilesia.si_learn.entities.compositeIds.UserLessonId;
import com.itsilesia.si_learn.entities.compositeIds.UserTaskId;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.courses.TaskRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserLessonRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserTaskRepository;
import com.itsilesia.si_learn.repositories.users.UserRepository;
import com.itsilesia.si_learn.services.online_compiler.OnlineCompilerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserTaskService {
	private final UserTaskRepository userTaskRepository;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final UserCourseHelperService userCourseHelperService;
	private final UserLessonRepository userLessonRepository;
	private final OnlineCompilerService onlineCompilerService;


	public @NonNull UserTask getTaskUser(UUID taskId, UUID userId) {
		var userTask = userTaskRepository.findById(new UserTaskId(userId, taskId));
		//it already exists return it
		if (userTask.isPresent())
			return userTask.get();

		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		var userLesson = userLessonRepository.findById(new UserLessonId(userId, task.getLesson().getId()))
			.orElse(null);
		//confirm that user has access to this resource
		userCourseHelperService.isAccessibleOrThrow(task, userId, userLesson);

		//add new user task
		var newUserTask = new UserTask();
		newUserTask.setTask(task);
		newUserTask.setLesson(task.getLesson());
		newUserTask.setUser(userRepository.getReferenceById(userId));

		return userTaskRepository.save(newUserTask);
	}

	/**
	 * Runs the code and returns compilation id. does not save a user solution.
	 *
	 * @param taskId  task id
	 * @param userId  user id
	 * @param request code and input to run
	 * @return compilation id
	 */
	public UUID runStudentCode(UUID taskId, UUID userId, SubmitExecuteTaskRequest request) {
		var userTask = this.getTaskUser(taskId, userId);
		return onlineCompilerService.executeRunOnlineAndSaveAsync(userTask, request.getCode(), request.getInput());
	}

	/**
	 * Returns compilation results by compilation id
	 *
	 * @param userId        user id
	 * @param compilationId the compilation id returned from {}/run
	 * @return compilation results
	 */
	@Transactional(readOnly = true)
	public CompilationResponse getCompilationDetails(UUID userId, UUID compilationId) {
		return onlineCompilerService.getCompilationResponse(userId, compilationId);
	}

	/**
	 * Runs auto tests on user solution using test scenarios and saves the user solution (for manual grading)
	 *
	 * @param taskId                task id
	 * @param userId                user id
	 * @param submitTestTaskRequest final code to submit and test
	 */
	@Transactional
	public void testStudentCodeWithTestScenario(UUID taskId, UUID userId, SubmitTestTaskRequest submitTestTaskRequest) {
		var userTask = getTaskUser(taskId, userId);
		userTask.setUserSolution(submitTestTaskRequest.getCode());
		userTask.setUpdatedSolutionAt(LocalDateTime.now());
		userTaskRepository.save(userTask);
		var task = userTask.getTask();
		onlineCompilerService.executeRunTestsOnlineAndSaveAsync(userTask, task);
	}
}
