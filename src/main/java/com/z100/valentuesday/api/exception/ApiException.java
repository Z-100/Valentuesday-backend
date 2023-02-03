package com.z100.valentuesday.api.exception;

import lombok.Getter;

public class ApiException extends RuntimeException {

	@Getter
	private String message;

	public ApiException() {
		super();
	}

	public ApiException(String message) {
		this.message = message;
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
