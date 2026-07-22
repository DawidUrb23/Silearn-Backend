package com.itsilesia.si_learn.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/// Conflict (409)
@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class ItemAlreadyExistsException extends BaseStatusException {
	private final Object id;

	public ItemAlreadyExistsException(Class<?> entityClass, Object id) {
		super(HttpStatus.CONFLICT, String.format("%s with ID: %s already exists", entityClass.getSimpleName(), id));
		this.id = id;
	}

	public ItemAlreadyExistsException(String message, Object id) {
		super(HttpStatus.CONFLICT, message);
		this.id = id;
	}
}
