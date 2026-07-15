package com.itsilesia.si_learn.services.user_progress;

import com.itsilesia.si_learn.entities.compositeIds.UserCourseId;
import com.itsilesia.si_learn.entities.compositeIds.UserModuleId;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.entities.user_progress.UserModule;
import com.itsilesia.si_learn.entities.users.User;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.exceptions.NotEnrolledInCourseException;
import com.itsilesia.si_learn.repositories.ModuleRepository;
import com.itsilesia.si_learn.repositories.UserCourseRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserModuleRepository;
import com.itsilesia.si_learn.repositories.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserModuleService {
	private final UserRepository userRepository;
	private final UserModuleRepository userModuleRepository;
	private final UserCourseRepository userCourseRepository;
	private final ModuleRepository moduleRepository;

	public UserModule getByIdAndStart(UUID userId, UUID moduleId) {
		var userModule = userModuleRepository.findById(new UserModuleId(userId, moduleId));
		if (userModule.isPresent())
			return userModule.get();

		//fetch user-module
		var module = moduleRepository.findById(moduleId)
			.orElseThrow(() -> new ItemNotFoundException(Module.class, moduleId));
		var user = userRepository.findById(userId)
			.orElseThrow(() -> new ItemNotFoundException(User.class, userId));
		//confirm that user is enrolled
		var course = module.getCourse();
		userCourseRepository.findById(new UserCourseId(userId, course.getId()))
			.orElseThrow(() -> new NotEnrolledInCourseException(userId, course));

		var newUserModule = new UserModule();
		newUserModule.setUser(user);
		newUserModule.setModule(module);
		newUserModule.setCourse(course);
		return userModuleRepository.save(newUserModule);
	}
}
