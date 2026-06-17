package com.itsilesia.si_learn.services;


import com.itsilesia.si_learn.entities.compositeIds.UserCourseId;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import com.itsilesia.si_learn.entities.user_progress.UserModule;
import com.itsilesia.si_learn.entities.users.User;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.CourseRepository;
import com.itsilesia.si_learn.repositories.UserCourseRepository;
import com.itsilesia.si_learn.repositories.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCourseService {

	private final CourseRepository courseRepository;
	private final UserRepository userRepository;
	private final UserCourseRepository userCourseRepository;

	public List<UserCourse> getUserCourses(UUID userId) {
		var user = userRepository.findById(userId)
			.orElseThrow(() -> new ItemNotFoundException(User.class, userId));
		return user.getUserCourses();
	}

	public UserCourse getUserCourse(UUID userId, UUID courseId) {
		var userCourse = userCourseRepository.findById(new UserCourseId(userId, courseId))
			.orElseThrow(() -> new ItemNotFoundException(UserCourse.class, "(user=" + userId + ", course=" + courseId + ")"));
		// Check if there are missing module tracking entries
		if (userCourse.getCourse().getModules().size() != userCourse.getUserModules().size()) {
			var userModules = userCourse.getUserModules();
			var modules = userCourse.getCourse().getModules();
			var course = userCourse.getCourse();
			// Gather all module IDs that already have a tracking entry for this user
			Set<UUID> existingModuleIds = userModules.stream()
				.map(um -> um.getModule().getId())
				.collect(Collectors.toSet());
			// Iterate through all modules in the course and identify the missing ones
			for (Module module : modules) {
				if (existingModuleIds.contains(module.getId()))
					continue;
				// Instantiate the missing progress tracking record
				UserModule missingModuleProgress = new UserModule();
				missingModuleProgress.setUser(userCourse.getUser());
				missingModuleProgress.setModule(module);
				missingModuleProgress.setCourse(course);
//				missingModuleProgress.setUserCourse(userCourse);
				missingModuleProgress.setStartDate(LocalDateTime.now());
				// Add it to the list (CascadeType.ALL on UserCourse should handle the save propagation)
				userModules.add(missingModuleProgress);
			}
			// Save parent to cascade persist operations down to the new UserModule entries
			userCourseRepository.save(userCourse);
		}
		return userCourse;
	}

	public UserCourse enroll(UUID courseId, UUID userId) {
		var user = userRepository.findById(userId)
			.orElseThrow(() -> new ItemNotFoundException(User.class, userId));
		var course = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));
		var userCourse = new UserCourse();
		userCourse.setUser(user);
		userCourse.setCourse(course);

		return userCourseRepository.save(userCourse);
	}
}
