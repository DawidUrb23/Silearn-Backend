package com.itsilesia.si_learn.controllers.users;

import com.itsilesia.si_learn.dtos.request.users.ChangePasswordRequest;
import com.itsilesia.si_learn.dtos.request.users.UpdateUserRequest;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.dtos.response.users.UserWithPersonalDataDto;
import com.itsilesia.si_learn.services.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Validated
@Tag(name = "Account", description = "Endpoints for managing user (Own) account")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {
	private final UserService userService;

	@PatchMapping("/password")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Operation(summary = "Change current user's password")
	@ApiResponse(responseCode = "204", description = "Password changed successfully")
	@ApiResponse(responseCode = "400", description = "Invalid request body",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
	public void ChangePassword(
		@RequestBody @Valid ChangePasswordRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		userService.changePassword(request, userId);
	}

	@PatchMapping
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Operation(summary = "Update current user's information")
	@ApiResponse(responseCode = "204", description = "User updated successfully")
	@ApiResponse(responseCode = "400", description = "Invalid request body",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
	public void UpdateUser(
		@RequestBody @Valid UpdateUserRequest request,
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		userService.updateUser(request, userId);
	}

	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Get current user's information")
	@ApiResponse(responseCode = "200", description = "User information returned successfully")
	@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
	@ApiResponse(responseCode = "404", description = "User not found",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public UserWithPersonalDataDto getUser(
		@Parameter(hidden = true) @AuthenticationPrincipal UUID userId
	) {
		return UserWithPersonalDataDto.fromEntity(
			userService.getById(userId)
		);
	}
}
