package com.itsilesia.si_learn.services.users;

import com.itsilesia.si_learn.dtos.request.users.LoginRequest;
import com.itsilesia.si_learn.services.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthService {
	private final UserService userService;
	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;

	public String login(LoginRequest loginRequest) {
		return userService.getByEmail(loginRequest.getEmail())
			.filter(user -> passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
			.map(jwtProvider::generateToken)
			.orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
	}
}
