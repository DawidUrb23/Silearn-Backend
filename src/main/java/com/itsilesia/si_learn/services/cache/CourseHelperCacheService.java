package com.itsilesia.si_learn.services.cache;


import com.itsilesia.si_learn.config.CacheConfig;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.services.courses.CourseHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true) // Keeps the Hibernate session alive to safely traverse lazy relations!
@RequiredArgsConstructor
public class CourseHelperCacheService {
	private final CourseHelperService courseHelperService;

	/**
	 * Gets only ids of children
	 *
	 * @param courseIds list of course ids
	 * @return a list of module ids
	 */
	@Cacheable(cacheNames = CacheConfig.coursesModulesCache, sync = true)
	public List<UUID> getAllModuleIdsIn(List<UUID> courseIds) {
		return courseHelperService.getAllModuleIdsIn(courseIds);
	}

	/**
	 * Gets only ids of children
	 *
	 * @param moduleIds list of module ids
	 * @return a list of lesson ids
	 */
	@Cacheable(cacheNames = CacheConfig.modulesLessonsCache, sync = true)
	public List<UUID> getAllLessonIdsIn(List<UUID> moduleIds) {
		return courseHelperService.getAllLessonIdsIn(moduleIds);
	}

	/**
	 * Gets only ids of children
	 *
	 * @param lessonIds list of lesson ids
	 * @return a list of task ids
	 */
	@Cacheable(cacheNames = CacheConfig.lessonsTasksCache, sync = true)
	public List<UUID> getAllTaskIdsIn(List<UUID> lessonIds, Specification<Task> filter) {
		return courseHelperService.getAllTaskIdsIn(lessonIds, filter);
	}


	/**
	 * Gets only ids of Tasks in Course
	 *
	 * @param courseId Course to get all Tasks from
	 * @param filter   Task filter
	 * @return a list of task ids
	 */
	@Cacheable(cacheNames = CacheConfig.coursesTasksCache, sync = true)
	public List<UUID> getAllTaskIdsInCourse(UUID courseId, Specification<Task> filter) {
		var allModuleIdsIn = courseHelperService.getAllModuleIdsIn(List.of(courseId));
		var allLessonIdsIn = courseHelperService.getAllLessonIdsIn(allModuleIdsIn);
		return courseHelperService.getAllTaskIdsIn(allLessonIdsIn, filter);
	}

}