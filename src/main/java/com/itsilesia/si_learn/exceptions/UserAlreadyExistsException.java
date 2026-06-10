package com.itsilesia.si_learn.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/// Conflict (409)
@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends ItemAlreadyExistsException {
	public UserAlreadyExistsException(Object id) {
		super(String.format("User \"%s\" already exists", id), id);
	}
}
