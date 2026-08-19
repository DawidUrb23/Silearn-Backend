package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.intermediate.UserTaskIdOnly;
import com.itsilesia.si_learn.dtos.request.users.GradeTaskRequest;
import com.itsilesia.si_learn.entities.compositeIds.UserTaskId;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.courses.CourseRepository;
import com.itsilesia.si_learn.repositories.courses.TaskRepository;
import com.itsilesia.si_learn.repositories.courses.TestScenarioRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserTaskRepository;
import com.itsilesia.si_learn.services.cache.CourseHelperCacheService;
import com.itsilesia.si_learn.services.courses.CourseHelperService;
import com.itsilesia.si_learn.services.user_progress.UserCourseHelperService;
import com.itsilesia.si_learn.specifications.TaskSpecification;
import com.itsilesia.si_learn.specifications.UserTaskSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskCheckService {
	private final CourseRepository courseRepository;
	private final TaskRepository taskRepository;
	private final UserTaskRepository userTaskRepository;
	private final CourseHelperService courseHelperService;
	private final TestScenarioRepository testScenarioRepository;
	private final UserCourseHelperService userCourseHelperService;
	private final CourseHelperCacheService courseHelperCacheService;

	public void gradeStudentSolution(UUID taskId, UUID studentId, @Valid GradeTaskRequest request, UUID teacherId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		courseHelperService.isAuthorOrThrow(task, teacherId);

		UserTask userTask = userTaskRepository.findById(new UserTaskId(studentId, taskId))
			.orElseThrow(() -> new ItemNotFoundException(UserTask.class, studentId));

		userTask.setTaskScore(request.grade());
		if (request.grade() >= task.getPassingScore())
			userTask.setPassed(true);

		userTask.setGradedAt(LocalDateTime.now());
		userTaskRepository.save(userTask);
	}

	public List<UserTaskIdOnly> getAllUserTasksToGrade(UUID courseId, UUID teacherId) {
		Course course = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));
		courseHelperService.isAuthorOrThrow(course, teacherId);

		var allTaskIdsWithManualGrading = courseHelperCacheService.getAllTaskIdsInCourse(courseId, TaskSpecification.isManuallyGraded());

		// Combine specifications using all of these checks
		Specification<UserTask> spec = Specification.allOf(
			UserTaskSpecification.hasTaskIdIn(allTaskIdsWithManualGrading),
			UserTaskSpecification.hasUserSolutionNotEmpty(),
			UserTaskSpecification.needsGrading()
		);
		return userCourseHelperService.getAllUserTaskIdsIn(allTaskIdsWithManualGrading, spec);
	}

	public List<UUID> getAllTasksInCourseToGrade(UUID courseId, UUID teacherId) {
		Course course = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));
		courseHelperService.isAuthorOrThrow(course, teacherId);

		return courseHelperCacheService.getAllTaskIdsInCourse(courseId, TaskSpecification.isManuallyGraded());
	}

	public List<UserTask> getAllTaskSolutionsToGrade(UUID taskId, UUID teacherId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		courseHelperService.isAuthorOrThrow(task, teacherId);
		return getUserTasks(taskId);
	}

	private List<UserTask> getUserTasks(UUID taskId) {
		//this task has automatic grading
		if (testScenarioRepository.existsByTaskId(taskId))
			return List.of();

		// Combine specifications using all of these checks
		Specification<UserTask> spec = Specification.allOf(
			UserTaskSpecification.hasTaskId(taskId),
			UserTaskSpecification.hasUserSolutionNotEmpty(),
			UserTaskSpecification.needsGrading()
		);
		return userTaskRepository.findAll(spec);
	}

	public void deleteStudentSolution(UUID taskId, UUID studentId, UUID teacherId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		courseHelperService.isAuthorOrThrow(task, teacherId);
		userTaskRepository.deleteById(new UserTaskId(studentId, taskId));
	}
}
