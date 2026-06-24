package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.courses.*;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.courses.TaskContent;
import com.itsilesia.si_learn.entities.courses.TestScenario;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.TaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final TaskRepository taskRepository;
	private final EntityManager entityManager;
	private final TranslationService translationService;


	public Task getTaskById(UUID id) {
		return taskRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, id));
	}

	@Transactional
	public Task createTask(CreateTaskRequest createTaskRequest) {
		Task newTask = new Task();

		newTask.setIndex(createTaskRequest.getIndex());
		newTask.setStartingCode(createTaskRequest.getStartingCode());
		newTask.setPassingScore(createTaskRequest.getPassingScore());
		Lesson lessonReference = entityManager.getReference(Lesson.class, createTaskRequest.getLessonId());
		newTask.setLesson(lessonReference);
		return taskRepository.save(newTask);
	}

	public void deleteTask(UUID id) {
		taskRepository.deleteById(id);
	}

	@Transactional
	public Task updateTask(UpdateTaskRequest taskDto, UUID id) {

		Task taskToUpdate = taskRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, id));

		if (taskDto.getStartingCode() != null && !taskDto.getStartingCode().isEmpty()) {
			taskToUpdate.setStartingCode(taskDto.getStartingCode());
		}
		if (taskDto.getPassingScore() != null) {
			taskToUpdate.setPassingScore(taskDto.getPassingScore());
		}
		if (taskDto.getTaskContent() != null) {
			Map<Integer, TaskContent> existingContentsMap = taskToUpdate.getTaskContents().stream()
				.collect(Collectors.toMap(TaskContent::getIndex, content -> content));
			for (UpdateTaskContentRequest dto : taskDto.getTaskContent()) {
				TaskContent existingContent = existingContentsMap.get(dto.getIndex());

				if (existingContent != null) {
					existingContent.setType(dto.getType());
					if (dto.getRequest() != null && existingContent.getContent() != null) {
						translationService.updateTranslationDictionary(
							existingContent.getContent().getId(),
							dto.getRequest()
						);
					}
					existingContentsMap.remove(dto.getIndex());
				} else {
					TaskContent newContent = new TaskContent();
					newContent.setIndex(dto.getIndex());
					newContent.setType(dto.getType());
					newContent.setTask(taskToUpdate);
					if (dto.getRequest() != null) {
						TranslationDictionary dictionary = translationService.createDictionary(
							dto.getRequest().getContent(),
							dto.getRequest().getLanguageCode()
						);
						newContent.setContent(dictionary);
					}
					taskToUpdate.getTaskContents().add(newContent);
				}
			}
			if (!existingContentsMap.isEmpty()) {
				taskToUpdate.getTaskContents().removeAll(existingContentsMap.values());
			}
		}
		return taskRepository.save(taskToUpdate);
	}


	public Task addContentToTask(UUID taskId, @Valid TaskContentRequest request) {
		Task taskToUpdate = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));

		TranslationDictionary dictionary = translationService.createDictionary(
			request.getTranslation().getContent(),
			request.getTranslation().getLanguageCode()
		);

		TaskContent newContent = new TaskContent();
		newContent.setTask(taskToUpdate);
		newContent.setIndex(request.getIndex());
		newContent.setContent(dictionary);
		newContent.setType(request.getContentType());

		taskToUpdate.getTaskContents().add(newContent);
		return taskRepository.save(taskToUpdate);
	}

	@Transactional
	public Task addScenarioToTask(UUID taskId, @Valid TestScenarioRequest request) {
		Task taskToUpdate = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		TestScenario newTestScenario = new TestScenario();
		newTestScenario.setTask(taskToUpdate);
		newTestScenario.setInput(request.getInput());
		newTestScenario.setExpectedOutput(request.getExpectedOutput());
		newTestScenario.setTimeoutSec(request.getTimeoutSec());

		taskToUpdate.getTestScenarios().add(newTestScenario);

		return taskRepository.save(taskToUpdate);
	}
}