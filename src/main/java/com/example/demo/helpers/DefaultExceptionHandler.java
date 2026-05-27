package com.example.demo.helpers;

import com.example.demo.dtos.response.exceptions.ErrorDetailsDto;
import com.example.demo.exceptions.InvalidParametersException;
import com.example.demo.exceptions.ItemNotFoundException;
import com.example.demo.exceptions.NoContentFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	/// No Content (204)
	@ExceptionHandler(NoContentFoundException.class)
	public ResponseEntity<@NotNull ErrorDetailsDto> handleInvalidParameters(NoContentFoundException ex) {
		return new ResponseEntity<@NotNull ErrorDetailsDto>(new ErrorDetailsDto(ex.getMessage()), HttpStatus.NO_CONTENT);
	}

}