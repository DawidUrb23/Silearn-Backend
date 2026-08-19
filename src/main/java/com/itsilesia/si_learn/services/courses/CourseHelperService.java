package com.itsilesia.si_learn.services.courses;

import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.entities.courses.*;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.exceptions.InvalidParametersException;
import com.itsilesia.si_learn.exceptions.NotOwnerException;
import com.itsilesia.si_learn.repositories.courses.LessonRepository;
import com.itsilesia.si_learn.repositories.courses.ModuleRepository;
import com.itsilesia.si_learn.repositories.courses.TaskContentRepository;
import com.itsilesia.si_learn.repositories.courses.TaskRepository;
import com.itsilesia.si_learn.specifications.LessonSpecification;
import com.itsilesia.si_learn.specifications.ModuleSpecification;
import com.itsilesia.si_learn.specifications.TaskContentSpecification;
import com.itsilesia.si_learn.specifications.TaskSpecification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true) // Keeps the Hibernate session alive to safely traverse lazy relations!
@RequiredArgsConstructor
public class CourseHelperService {
	private final ModuleRepository moduleRepository;
	private final LessonRepository lessonRepository;
	private final TaskRepository taskRepository;
	private final TaskContentRepository taskContentRepository;
	/// max length for checking in the DB using "WHERE ? IN()"
	private static final int maxCollectionLength = 10_000;

	public void isAuthorOrThrow(@NonNull Course course, UUID userId) {
		if (userId == null || !Objects.equals(course.getAuthor().getId(), userId))
			throw new NotOwnerException(userId, course.getId());
	}

	public void isAuthorOrThrow(Module module, UUID userId) {
		isAuthorOrThrow(module.getCourse(), userId);
	}

	public void isAuthorOrThrow(Lesson lesson, UUID userId) {
		isAuthorOrThrow(lesson.getModule(), userId);
	}

	public void isAuthorOrThrow(Task task, UUID userId) {
		isAuthorOrThrow(task.getLesson(), userId);
	}

	public void isAuthorOrThrow(TaskContent taskContent, UUID userId) {
		isAuthorOrThrow(taskContent.getTask(), userId);
	}

	public void isAuthorOrThrow(TestScenario testScenario, UUID userId) {
		isAuthorOrThrow(testScenario.getTask(), userId);
	}

	public Language getOriginalLanguage(Course course) {
		return course.getTitle().getOriginalLanguage();
	}

	public Language getOriginalLanguage(Module module) {
		return module.getTitle().getOriginalLanguage();
	}

	public Language getOriginalLanguage(Lesson lesson) {
		return lesson.getTopic().getOriginalLanguage();
	}

	public Language getOriginalLanguage(Task task) {
		return getOriginalLanguage(task.getLesson());
	}

	public Language getOriginalLanguage(TaskContent taskContent) {
		return taskContent.getContent().getOriginalLanguage();
	}

	/**
	 * Gets only ids of children
	 *
	 * @param courseIds list of course ids
	 * @return a list of module ids
	 */
	public List<UUID> getAllModuleIdsIn(List<UUID> courseIds) {
		if (courseIds == null || courseIds.isEmpty())
			return List.of();
		if (courseIds.size() >= maxCollectionLength)
			throw new InvalidParametersException("Collection limit exceeded. " + courseIds.size() + "/" + maxCollectionLength);

		return moduleRepository.findBy(
			ModuleSpecification.hasCourseIdIn(courseIds),
			query -> query.as(IdOnly.class).all()
		).stream().map(IdOnly::getId).toList();
	}

	/**
	 * Gets only ids of dictionaries
	 *
	 * @param moduleIds list of module ids
	 * @return a list of dictionary ids
	 */
	public List<UUID> getAllModuleDictionaryIdsIn(List<UUID> moduleIds) {
		if (moduleIds == null || moduleIds.isEmpty())
			return List.of();
		if (moduleIds.size() >= maxCollectionLength)
			throw new InvalidParametersException("Collection limit exceeded. " + moduleIds.size() + "/" + maxCollectionLength);

		var moduleSpecification = new ModuleSpecification();
		return moduleRepository.findBy(
			moduleSpecification.hasIdIn(moduleIds),
			query -> query.as(TitleIdOnly.class).all()
		).stream().map(TitleIdOnly::getTitleId).toList();
	}

	/**
	 * Gets only ids of children
	 *
	 * @param moduleIds list of module ids
	 * @return a list of lesson ids
	 */
	public List<UUID> getAllLessonIdsIn(List<UUID> moduleIds) {
		if (moduleIds == null || moduleIds.isEmpty())
			return List.of();
		if (moduleIds.size() >= maxCollectionLength)
			throw new InvalidParametersException("Collection limit exceeded. " + moduleIds.size() + "/" + maxCollectionLength);

		return lessonRepository.findBy(
			LessonSpecification.hasModuleIdIn(moduleIds),
			query -> query.as(IdOnly.class).all()
		).stream().map(IdOnly::getId).toList();
	}

	/**
	 * Gets only ids of dictionaries
	 *
	 * @param lessonIds list of module ids
	 * @return a list of dictionary ids
	 */
	public List<UUID> getAllLessonDictionaryIdsIn(List<UUID> lessonIds) {
		if (lessonIds == null || lessonIds.isEmpty())
			return List.of();
		if (lessonIds.size() >= maxCollectionLength)
			throw new InvalidParametersException("Collection limit exceeded. " + lessonIds.size() + "/" + maxCollectionLength);

		var lessonSpecification = new LessonSpecification();
		return lessonRepository.findBy(
				lessonSpecification.hasIdIn(lessonIds),
				query -> query.as(TopicDescriptionIdOnly.class).all()
			).stream()
			.map(it -> List.of(it.getDescriptionId(), it.getTopicId()))
			.flatMap(Collection::stream)
			.toList();
	}

	/**
	 * Gets only ids of children
	 *
	 * @param lessonIds list of lesson ids
	 * @return a list of task ids
	 */
	public List<UUID> getAllTaskIdsIn(List<UUID> lessonIds, Specification<Task> filter) {
		if (lessonIds == null || lessonIds.isEmpty())
			return List.of();
		if (lessonIds.size() >= maxCollectionLength)
			throw new InvalidParametersException("Collection limit exceeded. " + lessonIds.size() + "/" + maxCollectionLength);

		return taskRepository.findBy(
			Specification.allOf(
				filter,
				TaskSpecification.hasLessonIdIn(lessonIds)
			),
			query -> query.as(IdOnly.class).all()
		).stream().map(IdOnly::getId).toList();
	}

	/**
	 * Gets only ids of children
	 *
	 * @param taskIds list of Task ids
	 * @return a list of TaskContent ids
	 */
	public List<UUID> getAllTaskContentIdsIn(List<UUID> taskIds, Specification<TaskContent> filter) {
		if (taskIds == null || taskIds.isEmpty())
			return List.of();
		if (taskIds.size() >= maxCollectionLength)
			throw new InvalidParametersException("Collection limit exceeded. " + taskIds.size() + "/" + maxCollectionLength);

		return taskContentRepository.findBy(
			Specification.allOf(
				filter,
				TaskContentSpecification.hasTaskIdIn(taskIds)
			),
			query -> query.as(IdOnly.class).all()
		).stream().map(IdOnly::getId).toList();
	}

	/**
	 * Gets only ids of dictionaries
	 *
	 * @param taskContentIds list of Task ids
	 * @return a list of dictionary ids
	 */
	public List<UUID> getAllTaskContentDictionaryIdsIn(List<UUID> taskContentIds, Specification<TaskContent> filter) {
		if (taskContentIds == null || taskContentIds.isEmpty())
			return List.of();
		if (taskContentIds.size() >= maxCollectionLength)
			throw new InvalidParametersException("Collection limit exceeded. " + taskContentIds.size() + "/" + maxCollectionLength);

		var taskContentSpecification = new TaskContentSpecification();
		return taskContentRepository.findBy(
			Specification.allOf(
				filter,
				taskContentSpecification.hasIdIn(taskContentIds)
			),
			query -> query.as(ContentIdOnly.class).all()
		).stream().map(ContentIdOnly::getContentId).toList();
	}

	private interface IdOnly {
		UUID getId();
	}

	private interface TitleIdOnly {
		UUID getTitleId();
	}

	private interface TopicDescriptionIdOnly {
		UUID getTopicId();

		UUID getDescriptionId();
	}

	private interface ContentIdOnly {
		UUID getContentId();
	}
}