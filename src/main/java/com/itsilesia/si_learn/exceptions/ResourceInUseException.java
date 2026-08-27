package com.itsilesia.si_learn.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceInUseException extends Base409StatusException {

	public ResourceInUseException(Class<?> entityClass, Object id) {
		super(
			String.format(
				"%s with ID: %s is currently in use",
				entityClass.getSimpleName(),
				id
			));
	}
}
