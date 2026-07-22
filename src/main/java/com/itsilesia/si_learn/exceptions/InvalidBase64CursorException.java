package com.itsilesia.si_learn.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/// Bad Request (400)
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBase64CursorException extends Base400StatusException {
	private final Object object;

	public InvalidBase64CursorException(Object object) {
		super(String.format("failed to convert `%s` to/from Base64 String", object));
		this.object = object;
	}
}
