package com.itsilesia.si_learn.controllers.user_progress;

import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.dtos.response.user_progress.MyLessonWithTasksDto;
import com.itsilesia.si_learn.services.user_progress.UserLessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/my/lessons")
@RequiredArgsConstructor
@Validated
@Tag(name = "My lessons", description = "Endpoints for interacting with lesson progress (User)")
public class StudentLessonController {
	private final UserLessonService userLessonService;

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get task list with results for lesson")
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public MyLessonWithTasksDto getLessonTaskResults(
		@PathVariable UUID id,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		return MyLessonWithTasksDto.fromEntity(
			userLessonService.getByIdAndStart(userId, id)
		);
	}
}
