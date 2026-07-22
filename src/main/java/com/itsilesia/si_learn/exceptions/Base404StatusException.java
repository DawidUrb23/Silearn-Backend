package com.itsilesia.si_learn.exceptions;

import org.springframework.http.HttpStatus;

public class Base404StatusException extends BaseStatusException {
	public Base404StatusException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}
}
