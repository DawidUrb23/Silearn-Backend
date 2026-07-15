package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.users.GradeTaskRequest;
import com.itsilesia.si_learn.dtos.response.courses.TaskSubmissionResponse;
import com.itsilesia.si_learn.services.TaskCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("api/teacher-check")
@Tag(name = "Task Check", description = "Teacher gets solutions sent by users and can grade them (Teacher)")
public class TaskCheckTeacher {

	private final TaskCheckService taskCheckService;

	@GetMapping("/tasks/{taskId}/solutions")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Gets all solutions to specific task")
	public List<TaskSubmissionResponse> getAllSolutions(
		@PathVariable UUID taskId,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID teacherId
	) {
		return taskCheckService.getAllSolutions(taskId, teacherId);
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

	@Transactional
	@DeleteMapping("/tasks/{taskId}/students/{studentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete students solution")
	public void deleteStudentSolution(
		@PathVariable UUID taskId,
		@PathVariable UUID studentId,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID teacherId
	) {

		taskCheckService.deleteStudentSolution(taskId, studentId, teacherId);
	}
}
