package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.users.GradeTaskRequest;
import com.itsilesia.si_learn.dtos.response.courses.TaskSubmissionResponse;
import com.itsilesia.si_learn.dtos.response.courses.TaskUserResponse;
import com.itsilesia.si_learn.entities.compositeIds.UserTaskId;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.TaskRepository;
import com.itsilesia.si_learn.repositories.UserTaskRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
		if (request.grade() >= task.getPassingScore()) {
			userTask.setPassed(true);
			userTask.progressStatus();
		} else {
			userTask.setPassed(false);
			userTask.progressStatus();
		}

		userTaskRepository.save(userTask);

	}

	public List<TaskSubmissionResponse> getAllSolutions(UUID taskId, UUID teacherId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		courseHelperService.isAuthorOrThrow(task, teacherId);
		return userTaskRepository.findAll()
			.stream()
			.map(userTask -> TaskSubmissionResponse.fromEntity(
				userTask.getTask(),
				userTask
			))
			.toList();
	}

	public void deleteStudentSolution(UUID taskId, UUID studentId, UUID teacherId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		courseHelperService.isAuthorOrThrow(task,teacherId);
		userTaskRepository.deleteByTaskIdAndUserId(taskId,studentId);
	}
}
