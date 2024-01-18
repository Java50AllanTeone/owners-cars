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

import lombok.extern.slf4j.Slf4j;
import telran.cars.exceptions.NotFoundException;

@ControllerAdvice
@Slf4j
public class CarsExceptionsController {
	
	@ExceptionHandler(NotFoundException.class)
	ResponseEntity<String> notFoundHandler(NotFoundException e) {
		return returnResponse(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<String> returnResponse(String message, HttpStatus status) {
		log.error(message);
		return new ResponseEntity<String>(message, status);
	}
	
	@ExceptionHandler(IllegalStateException.class)
	ResponseEntity<String> alreadyExistsHandler(IllegalStateException e) {
		return returnResponse(e.getMessage(), HttpStatus.ALREADY_REPORTED);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<String> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
		String message = e.getAllErrors()
		.stream()
		.map(error -> error.getDefaultMessage())
		.collect(Collectors.joining(";"));
		
		return returnResponse(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HandlerMethodValidationException.class)
	ResponseEntity<String> methodValidationHandler(HandlerMethodValidationException e) {	
		String message = e.getAllErrors()
				.stream()
				.map(err -> err.getDefaultMessage())
				.collect(Collectors.joining(";"));
	
		return returnResponse(message, HttpStatus.BAD_REQUEST);
	}

}
