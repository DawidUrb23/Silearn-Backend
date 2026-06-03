package com.itsilesia.si_learn.services.users;


import com.itsilesia.si_learn.dtos.request.users.ChangePasswordRequest;
import com.itsilesia.si_learn.dtos.request.users.UpdateUserRequest;
import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.entities.enums.Role;
import com.itsilesia.si_learn.entities.users.User;
import com.itsilesia.si_learn.exceptions.InvalidParametersException;
import com.itsilesia.si_learn.exceptions.ItemAlreadyExistsException;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.users.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final RoleService roleService;
	private final LangService langService;
	private final PasswordEncoder passwordEncoder;

	public User changePassword(@NonNull ChangePasswordRequest request, UUID userId) {
		var user = userRepository.findById(userId).
			orElseThrow(() -> new ItemNotFoundException(User.class, userId));

		if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
			throw new InvalidParametersException("Incorrect Password.");
		}
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		return userRepository.save(user);
	}

	public User updateUser(@Valid UpdateUserRequest request, UUID userId) {
		var user = userRepository.findById(userId).
			orElseThrow(() -> new ItemNotFoundException(User.class, userId));

		if (request.getEmail() != null) {
			if (userRepository.existsByEmail(request.getEmail()))
				throw new ItemAlreadyExistsException(User.class, request.getEmail());
			user.setEmail(request.getEmail());
		}

		if (request.isBecomeTeacher())
			roleService.addRoleToUser(user, Role.TEACHER);

		if (!StringUtils.isBlank(request.getLanguageCode())) {
			var lang = langService.getLangByCode(request.getLanguageCode())
				.orElseThrow(() -> new ItemNotFoundException(Language.class, request.getLanguageCode()));
			user.setLanguage(lang);
		}

		return userRepository.save(user);
	}

	public User getUser(UUID userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new ItemNotFoundException(User.class, userId));
	}
}
