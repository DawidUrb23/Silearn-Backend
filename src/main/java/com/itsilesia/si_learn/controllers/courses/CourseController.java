package com.itsilesia.si_learn.controllers.courses;

import com.itsilesia.si_learn.dtos.request.courses.ContentsOrderUpdateRequest;
import com.itsilesia.si_learn.dtos.request.courses.CreateCourseRequest;
import com.itsilesia.si_learn.dtos.request.courses.CreateModuleRequest;
import com.itsilesia.si_learn.dtos.response.IdOnlyDto;
import com.itsilesia.si_learn.dtos.response.courses.CourseWithModulesResponse;
import com.itsilesia.si_learn.dtos.response.courses.KeysetCourseResponse;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.dtos.response.user_progress.MyCourseOverviewDto;
import com.itsilesia.si_learn.entities.abstractions.EntityWithOrderedChildren;
import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.entities.enums.DifficultyLevel;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.entities.enums.VisibilityStatus;
import com.itsilesia.si_learn.entities.user_progress.UserCourse;
import com.itsilesia.si_learn.helpers.CourseCursor;
import com.itsilesia.si_learn.helpers.CursorUtil;
import com.itsilesia.si_learn.services.CourseService;
import com.itsilesia.si_learn.services.ModuleService;
import com.itsilesia.si_learn.services.UserCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Validated
@Tag(name = "Courses", description = "Endpoints for: managing courses (Owner Teacher), getting course info (Public)")
public class CourseController {
	private final CourseService courseService;
	private final UserCourseService userCourseService;
	private final ModuleService moduleService;


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new course (Teacher)", description = "Creates a new Course")
	@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404", description = "Something does not exist",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public IdOnlyDto createCourse(
		@Valid @RequestBody CreateCourseRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		return IdOnlyDto.fromEntity(
			courseService.createCourse(request, userId)
		);
	}

	@PostMapping("/{courseId}/enroll")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Enroll into the course (Student)")
	@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void enrollToCourse(
		@PathVariable UUID courseId,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		userCourseService.enroll(courseId, userId);
	}


	@PostMapping("/{courseId}/modules")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new module for course (Owner)")
	@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public IdOnlyDto createModule(
		@PathVariable UUID courseId,
		@Valid @RequestBody CreateModuleRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		return IdOnlyDto.fromEntity(
			moduleService.createModule(courseId, request, userId)
		);
	}

	@PutMapping("/{courseId}/modules")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Update module order in course (Owner)")
	@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void UpdateModuleOrderInCourse(
		@PathVariable UUID courseId,
		@Valid @RequestBody ContentsOrderUpdateRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		moduleService.updateModuleOrder(courseId, request, userId);
	}

	@GetMapping("/{courseId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get specific course details with enrolment status")
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public CourseWithModulesResponse getCourse(
		@PathVariable UUID courseId,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId,
		@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String languageCode //exposes the header for LocaleContextHolder
	) {
		Course course = courseService.getCourse(courseId);
		UserCourse userCourse = userCourseService.getUserCourseOrNull(userId, courseId);
		return CourseWithModulesResponse.fromEntity(course, userCourse);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get courses with filters (Public)")
	public @NonNull KeysetCourseResponse getCourses(
		@RequestParam(required = false) VisibilityStatus status,
		@RequestParam(required = false) DifficultyLevel difficulty,
		@RequestParam(required = false) ProgramingLanguage programingLanguage,
		@RequestParam(required = false) String name,
		@RequestParam(required = false) UUID authorId,
		@RequestParam(required = false) String cursor,
		@RequestParam(defaultValue = "20") int size,
		@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String languageCode,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		var courseSlice = courseService.getFilteredCourses(status, difficulty, programingLanguage, name, authorId, cursor, size, userId);
		var userCourses = userCourseService.getUserCourses(userId);
		List<@NonNull Course> courses = courseSlice.getContent();
		var coursesWithUserCourses = EntityWithOrderedChildren.JoinItemWithUserItem(courseSlice.getContent(), userCourses);

		String nextCursor = null;
		if (courseSlice.hasNext() && !courses.isEmpty()) {
			Course lastCourse = courses.getLast();
			nextCursor = CursorUtil.encode(new CourseCursor(lastCourse.getCreatedAt(), lastCourse.getId()));
		}
		return new KeysetCourseResponse(
			coursesWithUserCourses.stream().map(MyCourseOverviewDto::fromEntity).toList(),
			nextCursor);

	}

}
