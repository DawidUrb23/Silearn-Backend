package com.itsilesia.si_learn.controllers.users;


import com.itsilesia.si_learn.dtos.request.users.LoginRequest;
import com.itsilesia.si_learn.dtos.request.users.RegisterRequest;
import com.itsilesia.si_learn.dtos.response.auth.JwtDto;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.services.users.JwtAuthService;
import com.itsilesia.si_learn.services.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {
	private final UserService userService;
	private final JwtAuthService jwtAuthService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Register a new user", description = "Creates a new user account based on the provided data.")
	@ApiResponse(responseCode = "204", description = "User registered successfully")
	@ApiResponse(responseCode = "400", description = "Invalid input, e.g., password is too weak",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "409", description = "email already exists",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public void register(
		@Valid @RequestBody RegisterRequest registerRequest
	) {
		userService.register(registerRequest);
	}

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Log in a user", description = "Authenticates a user and returns a JWT token upon successful login.")
	@ApiResponse(responseCode = "200", description = "Login successful, JWT token returned")
	@ApiResponse(responseCode = "400", description = "Invalid credentials",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public JwtDto login(
		@Valid @RequestBody LoginRequest loginRequest
	) {
		return new JwtDto(
			jwtAuthService.login(loginRequest)
		);
	}
}
