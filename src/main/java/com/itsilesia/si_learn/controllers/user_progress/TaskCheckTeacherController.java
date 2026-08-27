package com.itsilesia.si_learn.controllers.user_progress;

import com.itsilesia.si_learn.dtos.request.teacher_progress.GradeTaskRequest;
import com.itsilesia.si_learn.dtos.response.courses.TaskSubmissionsResponse;
import com.itsilesia.si_learn.services.courses.TaskService;
import com.itsilesia.si_learn.services.user_progress.TaskCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("api/teacher-check")
@Tag(name = "Task Check", description = "Teacher gets solutions sent by users and can grade them (Teacher)")
public class TaskCheckTeacherController {
	private final TaskService taskService;
	private final TaskCheckService taskCheckService;

	@GetMapping("/tasks/{taskId}/solutions")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Gets all user solutions to specific task")
	public TaskSubmissionsResponse getAllSolutions(
		@PathVariable UUID taskId,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID teacherId
	) {
		return TaskSubmissionsResponse.fromEntity(
			taskService.getTaskById(taskId),
			taskCheckService.getAllTaskSolutionsToGrade(taskId, teacherId)
		);
	}

	@GetMapping("/courses/{courseId}/tasks-to-grade")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Gets all task ids that need manual grading")
	public List<UUID> getAllTasks(
		@PathVariable UUID courseId,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID teacherId
	) {
		return taskCheckService.getAllTasksInCourseToGrade(courseId, teacherId);
	}


	@PostMapping("tasks/{taskId}/students/{studentId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Grades students exercise")
	public void gradeStudentSolution(
		@PathVariable UUID taskId,
		@PathVariable UUID studentId,
		@RequestBody @Valid GradeTaskRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID teacherId
	) {
		taskCheckService.gradeStudentSolution(taskId, studentId, request, teacherId);
	}
}
