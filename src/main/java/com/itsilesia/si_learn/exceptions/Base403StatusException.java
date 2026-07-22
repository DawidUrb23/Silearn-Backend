package com.itsilesia.si_learn.exceptions;

import org.springframework.http.HttpStatus;

public abstract class Base403StatusException extends BaseStatusException {
	public Base403StatusException(String message) {
		super(HttpStatus.FORBIDDEN, message);
	}
}
