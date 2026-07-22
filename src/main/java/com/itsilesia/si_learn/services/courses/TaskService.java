package com.itsilesia.si_learn.services.courses;

import com.itsilesia.si_learn.dtos.request.courses.*;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.entities.abstractions.EntityWithOrderedChildren;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.courses.TaskContent;
import com.itsilesia.si_learn.entities.courses.TestScenario;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.courses.LessonRepository;
import com.itsilesia.si_learn.repositories.courses.TaskContentRepository;
import com.itsilesia.si_learn.repositories.courses.TaskRepository;
import com.itsilesia.si_learn.repositories.courses.TestScenarioRepository;
import com.itsilesia.si_learn.services.TranslationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final TaskRepository taskRepository;
	private final TranslationService translationService;
	private final LessonRepository lessonRepository;
	private final CourseHelperService courseHelperService;
	private final TaskContentRepository taskContentRepository;
	private final TestScenarioRepository testScenarioRepository;

	@Transactional(readOnly = true)
	public Task getTaskById(UUID id) {
		return taskRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, id));
	}


	@Transactional
	public Task createTask(CreateTaskRequest createTaskRequest, UUID lessonId, UUID userId) {
		Lesson lessonReference = lessonRepository.findById(lessonId).
			orElseThrow(() -> new ItemNotFoundException(Lesson.class, lessonId));
		courseHelperService.isAuthorOrThrow(lessonReference, userId);

		Task newTask = new Task();
		newTask.setIndex(EntityWithOrderedChildren.getNextIndex(lessonReference.getTasks()));
		newTask.setStartingCode(createTaskRequest.getStartingCode());
		newTask.setPassingScore(createTaskRequest.getPassingScore());
		newTask.setLesson(lessonReference);
		return taskRepository.save(newTask);
	}

	@Transactional
	public void deleteTask(UUID id, UUID userId) {
		var task = taskRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, id));
		courseHelperService.isAuthorOrThrow(task, userId);
		taskRepository.delete(task);
	}

	@Transactional
	public Task updateTask(UpdateTaskRequest taskRequest, UUID taskId, UUID userId) {
		Task taskToUpdate = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		courseHelperService.isAuthorOrThrow(taskToUpdate, userId);

		if (taskRequest.getStartingCode() != null && !taskRequest.getStartingCode().isEmpty())
			taskToUpdate.setStartingCode(taskRequest.getStartingCode());

		if (taskRequest.getPassingScore() != null)
			taskToUpdate.setPassingScore(taskRequest.getPassingScore());

		return taskRepository.save(taskToUpdate);
	}

	@Transactional
	public TaskContent addTaskContent(UUID taskId, @Valid TaskContentRequest request, UUID userId) {
		Task taskToUpdate = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		courseHelperService.isAuthorOrThrow(taskToUpdate, userId);

		var taskContent = addContentToTask(taskToUpdate, request);

		return taskContentRepository.save(taskContent);
	}

	@Transactional
	public TestScenario addScenarioToTask(UUID taskId, @Valid TestScenarioRequest request, UUID userId) {
		Task taskToUpdate = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		courseHelperService.isAuthorOrThrow(taskToUpdate, userId);

		TestScenario newTestScenario = new TestScenario();
		newTestScenario.setTask(taskToUpdate);
		newTestScenario.setInput(request.getInput());
		newTestScenario.setExpectedOutput(request.getExpectedOutput());
		newTestScenario.setTimeoutSec(request.getTimeoutSec());

		taskToUpdate.getTestScenarios().add(newTestScenario);

		return testScenarioRepository.save(newTestScenario);
	}

	@Transactional
	public Task updateTaskContentsOrder(UUID taskId, @Valid ContentsOrderUpdateRequest request, UUID userId) {
		Task taskToUpdate = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		courseHelperService.isAuthorOrThrow(taskToUpdate, userId);
		//update the indexes
		EntityWithOrderedChildren.updateIndexesOfChildren(taskToUpdate.getTaskContents(), request);
		return taskRepository.save(taskToUpdate);
	}


	/**
	 * Adds task content into the provided Task, does not save to database
	 *
	 * @param taskToUpdate Task to update
	 * @param taskContent  TaskContent to add
	 * @return new TaskContent
	 */
	private TaskContent addContentToTask(Task taskToUpdate, TaskContentRequest taskContent) {
		var originalLanguage = courseHelperService.getOriginalLanguage(taskToUpdate);
		TranslationDictionary dictionary = translationService.createDictionary(
			taskContent.getContent(),
			originalLanguage
		);

		TaskContent newContent = new TaskContent();
		newContent.setTask(taskToUpdate);
		newContent.setIndex(EntityWithOrderedChildren.getNextIndex(taskToUpdate.getTaskContents()));
		newContent.setContent(dictionary);
		newContent.setType(taskContent.getType());

		taskToUpdate.getTaskContents().add(newContent);
		return newContent;
	}

	public Lesson updateTaskOrder(UUID lessonId, @Valid ContentsOrderUpdateRequest request, UUID userId) {
		var toUpdate = lessonRepository.findById(lessonId)
			.orElseThrow(() -> new ItemNotFoundException(Lesson.class, lessonId));
		courseHelperService.isAuthorOrThrow(toUpdate, userId);
		//update the indexes
		EntityWithOrderedChildren.updateIndexesOfChildren(toUpdate.getTasks(), request);
		return lessonRepository.save(toUpdate);
	}
}