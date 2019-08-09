package com.comments.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoResultException extends RuntimeException {
	public NoResultException(String message) {
		super(message);
	}
	public NoResultException(String message, Throwable cause) {
		super(message, cause);
	}

}

