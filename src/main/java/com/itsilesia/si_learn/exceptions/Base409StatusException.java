package com.itsilesia.si_learn.exceptions;

import org.springframework.http.HttpStatus;

public abstract class Base409StatusException extends BaseStatusException {

	public Base409StatusException(String message) {
		super(HttpStatus.CONFLICT, message);
	}
}