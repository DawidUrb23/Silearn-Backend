package com.itsilesia.si_learn.services.courses;

import com.itsilesia.si_learn.dtos.request.courses.CreateCourseRequest;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.enums.DifficultyLevel;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.entities.enums.VisibilityStatus;
import com.itsilesia.si_learn.entities.users.User;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.helpers.cursors.CourseCursor;
import com.itsilesia.si_learn.helpers.cursors.CursorUtil;
import com.itsilesia.si_learn.repositories.courses.CourseRepository;
import com.itsilesia.si_learn.repositories.user_progress.UserCourseRepository;
import com.itsilesia.si_learn.repositories.users.UserRepository;
import com.itsilesia.si_learn.services.TranslationService;
import com.itsilesia.si_learn.services.user_progress.UserCourseService;
import com.itsilesia.si_learn.specifications.CourseSpecification;
import com.itsilesia.si_learn.dtos.request.courses.UpdateCourseRequest;
import com.itsilesia.si_learn.exceptions.InvalidParametersException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

	private final CourseRepository courseRepository;
	private final UserRepository userRepository;
	private final TranslationService translationService;
	private final CourseHelperService courseHelperService;
	private final UserCourseService userCourseService;
	private final UserCourseRepository userCourseRepository;
	static private final int maxSize = 100;


	@Transactional
	public Course createCourse(CreateCourseRequest request, UUID authorId) {
		User author = userRepository.findById(authorId)
			.orElseThrow(() -> new ItemNotFoundException(User.class, authorId));

		Course course = new Course();
		course.setTitle(translationService.createDictionary(request.title(), request.languageCode()));
		course.setDescription(translationService.createDictionary(request.description(), request.languageCode()));
		course.setAuthor(author);
		course.setStatus(VisibilityStatus.DRAFT);
		course.setDifficulty(request.difficulty());
		course.setProgramingLanguage(request.programingLanguage());
		if (request.forceSequentialTasks() == null || !request.forceSequentialTasks())
			course.setForceSequentialTasks(false);
		else
			course.setForceSequentialTasks(true);

		Course savedCourse = courseRepository.save(course);
		userCourseService.createUserCourse(author, savedCourse);
		return savedCourse;
	}

	public Course getCourse(UUID courseId) {
		return courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));
	}

	public Course getCourseWithModules(UUID courseId, UUID userId) {
		var course = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));
		courseHelperService.isAuthorOrThrow(course, userId);

		return course;
	}

	public Slice<@NonNull Course> getFilteredCourses(
		VisibilityStatus status,
		DifficultyLevel difficulty,
		ProgramingLanguage programingLanguage,
		String query,
		UUID authorId,
		String cursor,
		int size,
		UUID userId
	) {
		if (size > maxSize) size = maxSize;
		if (size < 1) size = 1;
		var courseSpecification = new CourseSpecification();
		Specification<@NonNull Course> specification = Specification
			.where(CourseSpecification.hasStatus(status))  //static "ACTIVE"?, dynamic status
			.and(CourseSpecification.hasDifficulty(difficulty))
			.and(CourseSpecification.hasProgramingLanguage(programingLanguage))
			.and(CourseSpecification.matchesQuery(query))
			.and(CourseSpecification.hasAuthor(authorId))
			.and(courseSpecification.orderByCreatedAtDateId(Sort.Direction.DESC));

		if (cursor != null && !cursor.isBlank()) {
			CourseCursor decoded = CursorUtil.decode(CourseCursor.class, cursor);
			specification = specification.and(courseSpecification.isBefore(decoded.createdAt(), decoded.id()));
		}

		return courseRepository.findAllSliced(specification, PageRequest.of(0, size));
	}

	@Transactional
	public Course changeStatus(UUID courseId, VisibilityStatus status) {
		Course course = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));

		course.setStatus(status);

		return courseRepository.save(course);
	}

	@Transactional
	public Course updateCourse(UUID courseId, UpdateCourseRequest request, UUID userId) {
		Course course = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));

		courseHelperService.isAuthorOrThrow(course, userId);

		if (request.title() != null)
			translationService.updateOriginalTranslation(
				course.getTitle(),
				request.title()
			);

		if (request.description() != null)
			translationService.updateOriginalTranslation(
				course.getDescription(),
				request.description()
			);

		if (request.difficulty() != null)
			course.setDifficulty(request.difficulty());

		if (request.programingLanguage() != null)
			course.setProgramingLanguage(request.programingLanguage());

		if (request.forceSequentialTasks() != null)
			course.setForceSequentialTasks(request.forceSequentialTasks());

		if (request.status() != null) {
			if (
				request.status() != VisibilityStatus.DRAFT &&
					request.status() != VisibilityStatus.ACTIVE &&
					request.status() != VisibilityStatus.DELETED
			)
				throw new InvalidParametersException(
					"Author can set course status only to DRAFT, ACTIVE or DELETED"
				);

			course.setStatus(request.status());
		}

		return courseRepository.save(course);
	}

	@Transactional
	public void setStatusDeleted(UUID courseId) {
		Course course = courseRepository.findById(courseId)
			.orElseThrow(() -> new ItemNotFoundException(Course.class, courseId));
		course.setStatus(VisibilityStatus.DELETED);
		courseRepository.save(course);
	}
}