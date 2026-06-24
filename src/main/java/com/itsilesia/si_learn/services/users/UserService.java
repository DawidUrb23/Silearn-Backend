package com.itsilesia.si_learn.services.users;


import com.itsilesia.si_learn.dtos.request.users.ChangePasswordRequest;
import com.itsilesia.si_learn.dtos.request.users.RegisterRequest;
import com.itsilesia.si_learn.dtos.request.users.SetPasswordRequest;
import com.itsilesia.si_learn.dtos.request.users.UpdateUserRequest;
import com.itsilesia.si_learn.entities.Language;
import com.itsilesia.si_learn.entities.enums.Role;
import com.itsilesia.si_learn.entities.users.User;
import com.itsilesia.si_learn.exceptions.ItemAlreadyExistsException;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.exceptions.UserAlreadyExistsException;
import com.itsilesia.si_learn.repositories.users.UserRepository;
import com.itsilesia.si_learn.services.LanguageService;
import com.itsilesia.si_learn.specifications.UserSpecification;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final LanguageService languageService;
	private final RoleService roleService;
	private final LangService langService;
	private final PasswordEncoder passwordEncoder;

	public List<User> getAll(Role role) {
		return userRepository.findAll(UserSpecification.hasRole(role));
	}

	public User getById(UUID userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new ItemNotFoundException(User.class, userId));
	}

	public Optional<User> getByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User changePassword(@NonNull ChangePasswordRequest request, UUID userId) {
		var user = userRepository.findById(userId).
			orElseThrow(() -> new ItemNotFoundException(User.class, userId));

		//if there was no password then set it
		if (!user.getPassword().isEmpty() && !passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
			throw new BadCredentialsException("Incorrect Password.");
		}
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		return userRepository.save(user);
	}


	@Transactional
	public User register(@NotNull RegisterRequest registerRequest) {
		userRepository.findByEmail(registerRequest.getEmail()).ifPresent(user ->
		{
			throw new UserAlreadyExistsException(registerRequest.getEmail());
		});
		User user = new User();
		user.setEmail(registerRequest.getEmail());
		user.setPassword(registerRequest.getPassword());
		user.setName(registerRequest.getName());
		user.setSurname(registerRequest.getSurname());

		roleService.addRoleToUser(user, Role.STUDENT);
		if (registerRequest.isTeacherRole())
			roleService.addRoleToUser(user, Role.TEACHER);

		languageService.getLanguage(registerRequest.getLanguageCode())
			.ifPresent(user::setLanguage);

		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		return userRepository.save(user);
	}

	@Transactional
	public User updateUser(@Valid UpdateUserRequest request, UUID userId) {
		var user = userRepository.findById(userId).
			orElseThrow(() -> new ItemNotFoundException(User.class, userId));

		if (request.getEmail() != null) {
			if (userRepository.existsByEmail(request.getEmail()))
				throw new ItemAlreadyExistsException(User.class, request.getEmail());
			user.setEmail(request.getEmail());
		}

		if (request.getBecomeTeacher() != null && request.getBecomeTeacher())
			roleService.addRoleToUser(user, Role.TEACHER);

		if (StringUtils.isNotBlank(request.getLanguageCode())) {
			var lang = langService.getLangByCode(request.getLanguageCode())
				.orElseThrow(() -> new ItemNotFoundException(Language.class, request.getLanguageCode()));
			user.setLanguage(lang);
		}

		if (StringUtils.isNotBlank(request.getName()))
			user.setName(request.getName());
		if (StringUtils.isNotBlank(request.getSurname()))
			user.setSurname(request.getSurname());

		return userRepository.save(user);
	}

	public void deleteUser(UUID id) {
		var user = userRepository.findById(id)
			.orElseThrow(() -> new ItemNotFoundException(User.class, id));
		userRepository.delete(user);
	}

	public void setPassword(@NonNull SetPasswordRequest request, UUID id) {
		var user = userRepository.findById(id).
			orElseThrow(() -> new ItemNotFoundException(User.class, id));
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
	}
}
