package com.itsilesia.si_learn.services.cache;

import com.itsilesia.si_learn.config.CacheConfig;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.courses.CourseRepository;
import com.itsilesia.si_learn.services.courses.CourseHelperService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseDictionaryCacheService {
	private final CourseRepository courseRepository;
	private final CourseHelperService courseHelperService;

	@Cacheable(
		cacheNames = CacheConfig.courseDictionaryIdsCache,
		key = "#courseId",
		sync = true
	)
	public @NonNull List<UUID> getDictionaryUuidsInCourse(UUID courseId) {
		var course = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));

		List<UUID> itemIds = new ArrayList<>();

		itemIds.add(course.getTitle().getId());
		itemIds.add(course.getDescription().getId());

		var moduleIds = courseHelperService.getAllModuleIdsIn(
			List.of(courseId)
		);
		itemIds.addAll(
			courseHelperService.getAllModuleDictionaryIdsIn(moduleIds)
		);

		var lessonIds = courseHelperService.getAllLessonIdsIn(moduleIds);
		itemIds.addAll(
			courseHelperService.getAllLessonDictionaryIdsIn(lessonIds)
		);

		var taskIds = courseHelperService.getAllTaskIdsIn(
			lessonIds,
			Specification.unrestricted()
		);

		var taskContentIds = courseHelperService.getAllTaskContentIdsIn(
			taskIds,
			Specification.unrestricted()
		);

		itemIds.addAll(
			courseHelperService.getAllTaskContentDictionaryIdsIn(
				taskContentIds,
				Specification.unrestricted()
			)
		);

		return itemIds;
	}
}
