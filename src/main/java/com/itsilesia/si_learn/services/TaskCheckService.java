package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.users.GradeTaskRequest;
import com.itsilesia.si_learn.entities.compositeIds.UserTaskId;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.courses.TaskRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserTaskRepository;
import com.itsilesia.si_learn.services.courses.CourseHelperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskCheckService {

	private final TaskRepository taskRepository;
	private final UserTaskRepository userTaskRepository;
	private final CourseHelperService courseHelperService;

	public void gradeStudentSolution(UUID taskId, UUID studentId, @Valid GradeTaskRequest request, UUID teacherId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		courseHelperService.isAuthorOrThrow(task, teacherId);

		UserTask userTask = userTaskRepository.findById(new UserTaskId(studentId, taskId))
			.orElseThrow(() -> new ItemNotFoundException(UserTask.class, studentId));

		userTask.setTaskScore(request.grade());
		if (request.grade() >= task.getPassingScore())
			userTask.setPassed(true);

		userTaskRepository.save(userTask);
	}

	public List<UserTask> getAllSolutions(UUID taskId, UUID teacherId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		courseHelperService.isAuthorOrThrow(task, teacherId);
		return task.getUserTasks();
	}

	public void deleteStudentSolution(UUID taskId, UUID studentId, UUID teacherId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		courseHelperService.isAuthorOrThrow(task, teacherId);
		userTaskRepository.deleteById(new UserTaskId(studentId, taskId));
	}
}
