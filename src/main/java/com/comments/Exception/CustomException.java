package com.comments.Exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomException extends ResponseEntityExceptionHandler {
		
	private static final String BAD_REQUEST = "BAD_REQUEST";
	private static final String FILE_NOT_FOUND = "FILE_NOT_FOUND";
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());
		ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Empty or ivalid Youtube or Amazon URL is not allowed", errorList);
        return handleExceptionInternal(ex, errorDetails, headers, errorDetails.getStatus(), request);
    
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request){
		
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		
		
	}
	@ExceptionHandler(CustomNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleNotFoundException(CustomNotFoundException ex, WebRequest request){

		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse(FILE_NOT_FOUND, details);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		
	}
	@ExceptionHandler(NoResultException.class)
	public ResponseEntity<ErrorResponse> handleEmptyResult(NoResultException ex, WebRequest request){

		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse("There is no comments/reviews associated with this URL", details);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		
	}
	
	

}
