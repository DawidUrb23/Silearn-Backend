package com.itsilesia.si_learn.controllers.users;

import com.itsilesia.si_learn.dtos.request.users.SetPasswordRequest;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.dtos.response.users.UserWithPersonalDataDto;
import com.itsilesia.si_learn.entities.enums.Role;
import com.itsilesia.si_learn.services.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "Users", description = "Endpoints for managing user information (Admin)")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
	private final UserService userService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get all users with matching role (Admin)")
	public ResponseEntity<List<UserWithPersonalDataDto>> getAllUsers(
		@RequestParam(required = false) Role role
	) {
		return ResponseEntity.ok(
			userService.getAll(role)
				.stream().map(UserWithPersonalDataDto::fromEntity).toList()
		);
	}

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get user by id (Admin)")
	@ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public ResponseEntity<UserWithPersonalDataDto> getUser(@PathVariable UUID id) {
		return ResponseEntity.ok(
			UserWithPersonalDataDto.fromEntity(
				userService.getById(id)
			)
		);
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete user by id (Admin)")
	@ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("{id}/password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Force set user's password (Admin)")
	@ApiResponse(responseCode = "400", description = "Invalid request body",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public ResponseEntity<Void> setPassword(
		@PathVariable UUID id,
		@RequestBody SetPasswordRequest request
	) {
		userService.setPassword(request, id);
		return ResponseEntity.noContent().build();
	}
}
