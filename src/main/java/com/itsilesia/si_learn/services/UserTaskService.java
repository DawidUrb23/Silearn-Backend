package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.users.SubmitTaskRequest;
import com.itsilesia.si_learn.dtos.response.user_progress.TaskUserResponse;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.TaskRepository;
import com.itsilesia.si_learn.repositories.UserTaskRepository;
import com.itsilesia.si_learn.repositories.users.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserTaskService {

	private final UserTaskRepository userTaskRepository;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;

	public TaskUserResponse getTaskUser(UUID taskId, UUID userId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));

		UserTask userTask = userTaskRepository.findByTaskIdAndUserId(taskId, userId)
			.orElse(null);

		return TaskUserResponse.fromEntity(task, userTask);
	}

	public @NonNull UserTask submitTask(UUID taskId, UUID userId, SubmitTaskRequest request) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));

		UserTask userTask = userTaskRepository.findByTaskIdAndUserId(taskId, userId)
			.orElse(new UserTask());

		if (userTask.getUser() == null) {
			userTask.setTask(task);
			userTask.setLesson(task.getLesson());
			userTask.setUser(userRepository.getReferenceById(userId));
		}
		userTask.setTaskScore(null);
		userTask.setUserSolution(request.code());
		userTask.setPassed(false);

		return userTaskRepository.save(userTask);
	}
}