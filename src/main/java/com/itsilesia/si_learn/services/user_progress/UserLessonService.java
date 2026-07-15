package com.itsilesia.si_learn.services.user_progress;

import com.itsilesia.si_learn.entities.compositeIds.UserLessonId;
import com.itsilesia.si_learn.entities.compositeIds.UserModuleId;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.user_progress.UserLesson;
import com.itsilesia.si_learn.entities.users.User;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.LessonRepository;
import com.itsilesia.si_learn.repositories.UserLessonRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserModuleRepository;
import com.itsilesia.si_learn.repositories.users.UserRepository;
import com.itsilesia.si_learn.entities.user_progress.UserModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserLessonService {
	private final UserRepository userRepository;
	private final UserLessonRepository userLessonRepository;
	private final UserModuleRepository userModuleRepository;
	private final LessonRepository lessonRepository;

	public UserLesson getByIdAndStart(UUID userId, UUID lessonId) {
		var userLesson = userLessonRepository.findById(
			new UserLessonId(userId, lessonId)
		);

		if (userLesson.isPresent())
			return userLesson.get();

		var lesson = lessonRepository.findById(lessonId)
			.orElseThrow(() ->
				new ItemNotFoundException(Lesson.class, lessonId)
			);

		var user = userRepository.findById(userId)
			.orElseThrow(() ->
				new ItemNotFoundException(User.class, userId)
			);

		var module = lesson.getModule();

		var userModule = userModuleRepository.findById(
			new UserModuleId(userId, module.getId())
		).orElseThrow(() ->
			new ItemNotFoundException(
				UserModule.class,
				new UserModuleId(userId, module.getId())
			)
		);

		var newUserLesson = new UserLesson();
		newUserLesson.setUser(user);
		newUserLesson.setLesson(lesson);
		newUserLesson.setModule(module);
		newUserLesson.setUserModule(userModule);

		return userLessonRepository.save(newUserLesson);
	}
}
