package com.itsilesia.si_learn.helpers;

import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.exceptions.BaseStatusException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

	@ExceptionHandler(BaseStatusException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleBaseStatusException(BaseStatusException ex) {
		return new ResponseEntity<>(new ErrorDetailsDto(ex.getMessage(), ex.getHttpStatus()), ex.getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleUnexpectedException(Exception ex) {
		return new ResponseEntity<>(new ErrorDetailsDto(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleAuthenticationException(AuthenticationException ex) {
		return new ResponseEntity<>(new ErrorDetailsDto(ex.getMessage(), HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleInvalidArgumentException(MethodArgumentNotValidException ex) {
		String cleanMessage = ex.getBindingResult().getFieldErrors().stream()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.collect(Collectors.joining(", "));
		return new ResponseEntity<>(new ErrorDetailsDto(cleanMessage, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
	}
}