package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.courses.CreateTaskRequest;
import com.itsilesia.si_learn.dtos.request.courses.TaskContentRequest;
import com.itsilesia.si_learn.dtos.request.courses.TestScenarioRequest;
import com.itsilesia.si_learn.dtos.request.courses.UpdateTaskRequest;
import com.itsilesia.si_learn.dtos.response.courses.TaskResponse;
import com.itsilesia.si_learn.dtos.response.courses.TestScenarioResponse;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.services.TaskService;
import com.itsilesia.si_learn.services.TestScenarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;
	private final TestScenarioService testScenarioService;

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public TaskResponse getTask(@PathVariable UUID id) {
		Task task = taskService.getTaskById(id);
		return TaskResponse.taskDtoMapper(task);
	}

	@GetMapping("/{id}/test-scenarios")
	@ResponseStatus(HttpStatus.OK)
	public List<TestScenarioResponse> getTestScenarioByTaskId(@PathVariable("id") UUID taskId) {
		return testScenarioService.getTestScenarioByTaskId(taskId);
	}

	@PostMapping
	public ResponseEntity<TaskResponse> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
		Task createdTask = taskService.createTask(createTaskRequest);
		TaskResponse response = TaskResponse.taskDtoMapper(createdTask);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTask(@PathVariable UUID id) {
		taskService.deleteTask(id);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<TaskResponse> updateTask(@RequestBody UpdateTaskRequest taskDto, @PathVariable UUID id) {
		Task updatedTask = taskService.updateTask(taskDto, id);
		TaskResponse taskResponse = TaskResponse.taskDtoMapper(updatedTask);
		return new ResponseEntity<>(taskResponse, HttpStatus.OK);
	}

	@PostMapping("/{taskId}/contents")
	public ResponseEntity<TaskResponse> addContentToTask(@PathVariable("taskId") UUID taskId, @RequestBody @Valid TaskContentRequest request) {
		Task updatedTask = taskService.addContentToTask(taskId, request);

		TaskResponse taskResponse = TaskResponse.taskDtoMapper(updatedTask);

		return new ResponseEntity<>(taskResponse, HttpStatus.CREATED);
	}

	@PostMapping("/{taskId}/test-scenarios")
	public ResponseEntity<TaskResponse> addScenarioToTask(@PathVariable("taskId") UUID taskId, @RequestBody @Valid TestScenarioRequest request) {

		Task updateTask = taskService.addScenarioToTask(taskId, request);

		TaskResponse taskResponse = TaskResponse.taskDtoMapper(updateTask);

		return new ResponseEntity<>(taskResponse, HttpStatus.CREATED);
	}


}