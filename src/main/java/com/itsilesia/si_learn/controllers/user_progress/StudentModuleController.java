package com.itsilesia.si_learn.controllers.user_progress;

import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.dtos.response.user_progress.MyModuleWithLessonsDto;
import com.itsilesia.si_learn.services.user_progress.UserModuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/my/modules")
@RequiredArgsConstructor
@Validated
@Tag(name = "My modules", description = "Endpoints for interacting with enrolled modules (Enrolled Student)")
public class StudentModuleController {
	private final UserModuleService userModuleService;

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get the module with user progress")
	@ApiResponse(responseCode = "404",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public MyModuleWithLessonsDto getUserModule(
		@PathVariable UUID id,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId,
		@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String languageCode   //exposes the header for LocaleContextHolder
	) {
		return MyModuleWithLessonsDto.fromEntity(
			userModuleService.getByIdAndStart(userId, id)
		);
	}
}
