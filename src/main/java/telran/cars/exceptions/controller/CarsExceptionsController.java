package telran.cars.exceptions.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import telran.cars.exceptions.NotFoundException;

@ControllerAdvice
public class CarsExceptionsController {
	
	@ExceptionHandler(NotFoundException.class)
	ResponseEntity<String> notFoundHandler(NotFoundException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(IllegalStateException.class)
	ResponseEntity<String> alreadyExistsHandler(IllegalStateException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.ALREADY_REPORTED);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<String> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
		String message = e.getAllErrors()
		.stream()
		.map(error -> error.getDefaultMessage())
		.collect(Collectors.joining(";"));
		
		return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HandlerMethodValidationException.class)
	ResponseEntity<String> methodValidationHandler(HandlerMethodValidationException e) {	
		String message = e.getAllErrors()
				.stream()
				.map(err -> err.getDefaultMessage())
				.collect(Collectors.joining(";"));
	
		return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
	}

}