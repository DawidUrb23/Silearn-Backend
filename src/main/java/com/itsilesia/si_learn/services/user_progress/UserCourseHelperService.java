package com.itsilesia.si_learn.services.user_progress;

import com.itsilesia.si_learn.dtos.intermediate.UserTaskIdOnly;
import com.itsilesia.si_learn.entities.abstractions.CourseAccessNode;
import com.itsilesia.si_learn.entities.abstractions.UserProgressNode;
import com.itsilesia.si_learn.entities.compositeIds.UserCourseId;
import com.itsilesia.si_learn.entities.compositeIds.UserLessonId;
import com.itsilesia.si_learn.entities.compositeIds.UserModuleId;
import com.itsilesia.si_learn.entities.compositeIds.UserTaskId;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import com.itsilesia.si_learn.exceptions.CourseIsSequentialException;
import com.itsilesia.si_learn.exceptions.CourseNotAccessibleException;
import com.itsilesia.si_learn.exceptions.InvalidParametersException;
import com.itsilesia.si_learn.exceptions.NotEnrolledInCourseException;
import com.itsilesia.si_learn.repositories.user_progress.UserCourseRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserLessonRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserModuleRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserTaskRepository;
import com.itsilesia.si_learn.specifications.UserTaskSpecification;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = false) // Keeps the Hibernate session alive to safely traverse lazy relations!
@RequiredArgsConstructor
public class UserCourseHelperService {
	private final UserCourseRepository userCourseRepository;
	private final UserModuleRepository userModuleRepository;
	private final UserLessonRepository userLessonRepository;
	private final UserTaskRepository userTaskRepository;
	/// max length for checking in the DB using "WHERE ? IN()"
	private static final int maxCollectionLength = 10_000;

	/**
	 * Checks if user can gain access to (Resource) whether he is enrolled in a Course and if it is sequential check if previous task are complete
	 *
	 * @param node         the resource the user wants to access
	 * @param userId       the user id
	 * @param userProgress the parent of (node) resource that is bound to user
	 * @throws NotEnrolledInCourseException user is not enrolled in the course
	 * @throws CourseIsSequentialException  course is sequential
	 */
	@Transactional(readOnly = true)
	public void isAccessibleOrThrow(@NotNull CourseAccessNode node, @NotNull UUID userId, UserProgressNode userProgress) {
		var course = node.getCourse();
		this.isAccessibleOrThrow(course, userId);
		//user does not have access to course
		if (userProgress == null)
			throw new NotEnrolledInCourseException(userId, course);
		//course is not sequential, access in any order is permitted
		if (!course.isForceSequentialTasks())
			return;
		// Perform sequential checks for the current level
		final int currentIndex = node.getIndex();
		// Locate the immediate previous sibling element
		var previousItem = node.getSiblings().stream()
			.filter(it -> it.getIndex() < currentIndex)
			.max(Comparator.comparingInt(CourseAccessNode::getIndex));
		//this is the first item
		if (previousItem.isEmpty())
			return;
		var previousUserItem = this.getUserProgressEntity(userId, previousItem.get());

		//user did not complete the previous item
		if (previousUserItem == null || previousUserItem.progressStatus() != ProgressStatus.FINISHED)
			throw new CourseIsSequentialException(node.getCourse(), previousItem.get());
	}

	@Transactional(readOnly = true)
	public void isAccessibleOrThrow(@NotNull Course course, @Nullable UUID userId) {
		//author can access at any time his course
		if (userId != null && userId.equals(course.getAuthor().getId())) return;
		//this is as a switch statement for later if we decide that there should be more statuses that user can access
		switch (course.getStatus()) {
			case ACTIVE -> {
				return;
			}
			default -> throw new CourseNotAccessibleException(course);
		}
	}

	private UserProgressNode getUserProgressEntity(UUID userId, CourseAccessNode node) {
		if (node instanceof Course)
			return userCourseRepository.findById(new UserCourseId(userId, node.getId())).orElse(null);
		else if (node instanceof Module)
			return userModuleRepository.findById(new UserModuleId(userId, node.getId())).orElse(null);
		else if (node instanceof Lesson)
			return userLessonRepository.findById(new UserLessonId(userId, node.getId())).orElse(null);
		else if (node instanceof Task)
			return userTaskRepository.findById(new UserTaskId(userId, node.getId())).orElse(null);

		return null;
	}

	/**
	 * Propagates user progress up, when all items in parent are completed marks the parent completed.
	 * This helper does not save the changes to the DB.
	 *
	 * @param node the task to propagate
	 */
	@Transactional
	public void propagateUpdateProgress(UserProgressNode node) {
		// nothing to change
		if (node.progressStatus() != ProgressStatus.FINISHED)
			return;
		UserProgressNode parent = node.getParentProgressNode();
		if (parent == null)
			return; // Reached UserCourse root node, propagation ends successfully!

		var siblingProgress = node.getSiblingProgressNodes();
		var blueprintNodes = node.getBlueprintNodes();
		// not all siblings exist
		if (blueprintNodes.size() != siblingProgress.size())
			return;
		// Not all sibling are fully finished
		if (siblingProgress.stream().anyMatch(sibling -> sibling.progressStatus() != ProgressStatus.FINISHED))
			return;

		// Complete the parent node and recursively trigger bubble up
		parent.setEndDate(LocalDateTime.now());
		propagateUpdateProgress(parent);
	}


	/**
	 * Gets only ids of children
	 *
	 * @param taskIds list of task ids
	 * @return a list of user task ids
	 */
	public List<UserTaskIdOnly> getAllUserTaskIdsIn(List<UUID> taskIds, Specification<UserTask> filter) {
		if (taskIds == null || taskIds.isEmpty())
			return List.of();
		if (taskIds.size() > maxCollectionLength)
			throw new InvalidParametersException("Collection limit exceeded. " + taskIds.size() + "/" + maxCollectionLength);


		return userTaskRepository.findBy(
				Specification.allOf(
					filter,
					UserTaskSpecification.hasTaskIdIn(taskIds)
				),
				query -> query.as(UserTaskIdOnly.class).all()
			).stream()
			.toList();
	}

}
