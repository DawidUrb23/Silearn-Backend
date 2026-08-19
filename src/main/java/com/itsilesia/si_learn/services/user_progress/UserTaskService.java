package com.itsilesia.si_learn.services.user_progress;

import com.itsilesia.si_learn.dtos.request.users.SubmitTaskRequest;
import com.itsilesia.si_learn.entities.compositeIds.UserLessonId;
import com.itsilesia.si_learn.entities.compositeIds.UserTaskId;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.courses.TaskRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserLessonRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserTaskRepository;
import com.itsilesia.si_learn.repositories.users.UserRepository;
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
	private final UserTaskGradingService userTaskGradingService;


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

	/// @TODO: merge this method into test user code (only test)
	@Transactional
	public @NonNull UserTask submitTask(UUID taskId, UUID userId, SubmitTaskRequest request) {
		var userTask = this.getTaskUser(taskId, userId);
		userTask.setUserSolution(request.code());
		userTask.setUpdatedSolutionAt(LocalDateTime.now());
		userTaskGradingService.gradeUserSolutionAutomatically(userTask, request);
		userCourseHelperService.propagateUpdateProgress(userTask);

		return userTaskRepository.save(userTask);
	}
}
