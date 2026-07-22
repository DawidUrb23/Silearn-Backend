package com.itsilesia.si_learn.exceptions;

import org.springframework.http.HttpStatus;

public abstract class Base400StatusException extends BaseStatusException {
	public Base400StatusException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}
}
