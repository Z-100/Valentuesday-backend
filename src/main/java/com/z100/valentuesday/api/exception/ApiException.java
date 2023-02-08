package com.z100.valentuesday.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

	@Getter
	private String message;

	@Getter
	private HttpStatus status = HttpStatus.BAD_REQUEST;

	public ApiException() {
		super();
	}

	public ApiException(String message) {
		this.message = message;
	}

	public ApiException(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
