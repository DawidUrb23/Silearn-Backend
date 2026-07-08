package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.courses.CreateTaskRequest;
import com.itsilesia.si_learn.dtos.request.courses.TaskContentRequest;
import com.itsilesia.si_learn.dtos.request.courses.TestScenarioRequest;
import com.itsilesia.si_learn.dtos.request.courses.UpdateTaskRequest;
import com.itsilesia.si_learn.dtos.request.dictionary.UpdateTranslationDictionaryRequest;
import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.entities.TranslationDictionary;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.courses.TaskContent;
import com.itsilesia.si_learn.entities.courses.TestScenario;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.LessonRepository;
import com.itsilesia.si_learn.repositories.TaskRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
	private final TranslationService translationService;
	private final LessonRepository lessonRepository;
	private final CourseHelperService courseHelperService;

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

		newTask.setIndex(createTaskRequest.getIndex());
		newTask.setStartingCode(createTaskRequest.getStartingCode());
		newTask.setPassingScore(createTaskRequest.getPassingScore());
		newTask.setLesson(lessonReference);
		return taskRepository.save(newTask);
	}

	public void deleteTask(UUID id, UUID userId) {
		var task = taskRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, id));
		courseHelperService.isAuthorOrThrow(task, userId);
		taskRepository.delete(task);
	}

	@Transactional
	public Task updateTask(UpdateTaskRequest taskRequest, UUID id, UUID userId) {
		Task taskToUpdate = taskRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, id));
		courseHelperService.isAuthorOrThrow(taskToUpdate, userId);
		var courseLanguage = courseHelperService.getCourseLanguage(taskToUpdate);

		if (taskRequest.getStartingCode() != null && !taskRequest.getStartingCode().isEmpty())
			taskToUpdate.setStartingCode(taskRequest.getStartingCode());

		if (taskRequest.getPassingScore() != null)
			taskToUpdate.setPassingScore(taskRequest.getPassingScore());

		//perform a very efficient update of task contents
		if (taskRequest.getTaskContents() != null) {
			//this line expects that `index` in task does not repeat (is unique in task)
			Map<Integer, TaskContent> existingContentsMap = taskToUpdate.getTaskContents().stream()
				.collect(Collectors.toMap(TaskContent::getIndex, content -> content));
			int nextIndex = getMaxIndex(existingContentsMap) + 1;
			for (var updateTaskContent : taskRequest.getTaskContents()) {
				TaskContent existingContent = existingContentsMap.get(updateTaskContent.getIndex());

				if (existingContent != null) {
					existingContent.setType(updateTaskContent.getType());
					translationService.updateTranslationDictionary(
						existingContent.getContent().getId(),
						new UpdateTranslationDictionaryRequest(
							updateTaskContent.getContent(), courseLanguage.getCode())
					);
					//this line expects that `index` in task does not repeat (is unique in task)
					existingContentsMap.remove(updateTaskContent.getIndex());
				} else {
					TaskContent newContent = makeContent(taskToUpdate, updateTaskContent, courseLanguage, nextIndex++);
					taskToUpdate.getTaskContents().add(newContent);
				}
			}
			//remove all unused contents
			if (!existingContentsMap.isEmpty())
				taskToUpdate.getTaskContents().removeAll(existingContentsMap.values());
		}
		return taskRepository.save(taskToUpdate);
	}


	public Task addContentToTask(UUID taskId, @Valid TaskContentRequest request, UUID userId) {
		Task taskToUpdate = taskRepository.findById(taskId)
			.orElseThrow(() -> new ItemNotFoundException(Task.class, taskId));
		courseHelperService.isAuthorOrThrow(taskToUpdate, userId);
		var courseLanguage = courseHelperService.getCourseLanguage(taskToUpdate);
		//this line expects that `index` in task does not repeat (is unique in task)
		Map<Integer, TaskContent> existingContentsMap = taskToUpdate.getTaskContents().stream()
			.collect(Collectors.toMap(TaskContent::getIndex, content -> content));
		int maxIndex = getMaxIndex(existingContentsMap);

		TaskContent newContent = makeContent(taskToUpdate, request, courseLanguage, maxIndex + 1);

		taskToUpdate.getTaskContents().add(newContent);
		return taskRepository.save(taskToUpdate);
	}

	/**
	 * Creates a new TaskContent
	 *
	 * @param task           parent task
	 * @param request        content
	 * @param courseLanguage language
	 * @param indexFallback  index to be used if `request.index` is null or already exists
	 * @return new TaskContent
	 */
	private TaskContent makeContent(Task task, @Valid TaskContentRequest request, Language courseLanguage, @NotNull Integer indexFallback) {
		TranslationDictionary dictionary = translationService.createDictionary(
			request.getContent(),
			courseLanguage
		);

		TaskContent newContent = new TaskContent();
		newContent.setTask(task);
		final var index = request.getIndex();
		if (index != null && task.getTaskContents().stream()
			.noneMatch(item -> item.getIndex() == index))
			newContent.setIndex(index);
		else
			newContent.setIndex(indexFallback);
		newContent.setContent(dictionary);
		newContent.setType(request.getType());

		return newContent;
	}

	private int getMaxIndex(Map<Integer, TaskContent> existingContentsMap) {
		if (existingContentsMap == null || existingContentsMap.isEmpty())
			return 0;
		return existingContentsMap.keySet().stream()
			.mapToInt(Integer::intValue)
			.max()
			.orElse(0);
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

		taskRepository.save(taskToUpdate);
		return newTestScenario;
	}


}