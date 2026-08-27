package com.itsilesia.si_learn.exceptions;

import org.springframework.http.HttpStatus;

public abstract class Base204StatusException extends BaseStatusException {

	public Base204StatusException(String message) {
		super(HttpStatus.NO_CONTENT, message);
	}
}