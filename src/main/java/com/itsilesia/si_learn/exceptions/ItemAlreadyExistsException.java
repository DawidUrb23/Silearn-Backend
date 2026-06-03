package com.itsilesia.si_learn.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

///Conflict (409)
@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class ItemAlreadyExistsException extends RuntimeException {
	private final Object id;

	public ItemAlreadyExistsException(Class<?> entityClass, Object id) {
		super(String.format("%s with ID: %s already exists", entityClass.getSimpleName(), id));
		this.id = id;
	}
}
