package com.itsilesia.si_learn.controllers.users;

import com.itsilesia.si_learn.dtos.request.users.ChangePasswordRequest;
import com.itsilesia.si_learn.dtos.request.users.UpdateUserRequest;
import com.itsilesia.si_learn.dtos.response.users.UserDto;
import com.itsilesia.si_learn.services.users.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
	private final UserService userService;

	@PatchMapping("/password")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void ChangePassword(
		@RequestBody @Valid ChangePasswordRequest request,
		@RequestParam UUID userId
	) {
		userService.changePassword(request, userId);
	}

	@PatchMapping
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void UpdateUser(
		@RequestBody @Valid UpdateUserRequest request,
		@RequestParam UUID userId
	) {
		userService.updateUser(request, userId);
	}

	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	public UserDto getUser(
		@RequestParam UUID userId
	) {
		return UserDto.fromEntity(
			userService.getUser(userId)
		);
	}
}
