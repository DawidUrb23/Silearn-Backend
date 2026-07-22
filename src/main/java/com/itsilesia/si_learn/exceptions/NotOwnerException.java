package com.itsilesia.si_learn.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/// Not Authorized to do this (403)
@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotOwnerException extends Base403StatusException {
	private final Object userId;
	private final Object itemId;

	public NotOwnerException(Object userId, Object itemId) {
		super(String.format("User \"%s\" is not the owner of \"%s\" resource", userId, itemId));
		this.userId = userId;
		this.itemId = itemId;
	}
}
