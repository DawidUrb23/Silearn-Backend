package com.itsilesia.si_learn.helpers;

import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.exceptions.*;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

	/// bad argument (400)
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		return new ResponseEntity<@NotNull ErrorDetailsDto>(new ErrorDetailsDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	/// Not Found (404)
	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleItemNotFound(ItemNotFoundException ex) {
		return new ResponseEntity<@NotNull ErrorDetailsDto>(new ErrorDetailsDto(ex.getMessage()), HttpStatus.NOT_FOUND);
	}

	/// Invalid Parameters (400)
	@ExceptionHandler(InvalidParametersException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleInvalidParameters(InvalidParametersException ex) {
		return new ResponseEntity<@NotNull ErrorDetailsDto>(new ErrorDetailsDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	/// Authentication Exception (400)
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleInvalidParameters(AuthenticationException ex) {
		return new ResponseEntity<@NotNull ErrorDetailsDto>(new ErrorDetailsDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	/// CourseIsSequential Exception (400)
	@ExceptionHandler(CourseIsSequentialException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleInvalidParameters(CourseIsSequentialException ex) {
		return new ResponseEntity<@NotNull ErrorDetailsDto>(new ErrorDetailsDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	/// CourseNotAccessibleException Exception (400)
	@ExceptionHandler(CourseNotAccessibleException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleInvalidParameters(CourseNotAccessibleException ex) {
		return new ResponseEntity<@NotNull ErrorDetailsDto>(new ErrorDetailsDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	/// NotOwnerException Forbidden Exception (403)
	@ExceptionHandler(NotOwnerException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleInvalidParameters(NotOwnerException ex) {
		return new ResponseEntity<@NotNull ErrorDetailsDto>(new ErrorDetailsDto(ex.getMessage()), HttpStatus.FORBIDDEN);
	}

	/// NotEnrolledInCourseException Forbidden Exception (403)
	@ExceptionHandler(NotEnrolledInCourseException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleInvalidParameters(NotEnrolledInCourseException ex) {
		return new ResponseEntity<@NotNull ErrorDetailsDto>(new ErrorDetailsDto(ex.getMessage()), HttpStatus.FORBIDDEN);
	}

	/// already exists, Conflict (409)
	@ExceptionHandler(ItemAlreadyExistsException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleInvalidParameters(ItemAlreadyExistsException ex) {
		return new ResponseEntity<@NotNull ErrorDetailsDto>(new ErrorDetailsDto(ex.getMessage()), HttpStatus.CONFLICT);
	}

	/// No Content (204)
	@ExceptionHandler(NoContentFoundException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleInvalidParameters(NoContentFoundException ex) {
		return new ResponseEntity<@NotNull ErrorDetailsDto>(new ErrorDetailsDto(ex.getMessage()), HttpStatus.NO_CONTENT);
	}

}