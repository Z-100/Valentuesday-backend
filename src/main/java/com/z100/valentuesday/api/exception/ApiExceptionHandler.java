package com.z100.valentuesday.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ApiException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> handleApiRequestException(ApiException e) {

		ApiError error = new ApiError(
				e.getMessage(),
				e.getStatus(),
				e.getStatus().value(),
				ZonedDateTime.now(ZoneId.of("Z"))
		);

		return ResponseEntity.status(error.status())
				.contentType(APPLICATION_JSON)
				.body(error);
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	public ResponseEntity<ApiError> handleRuntimeException(RuntimeException e) {

		// TODO Fix this: Default response is access denied
		ApiError error = new ApiError(
				e.getMessage(),
				INTERNAL_SERVER_ERROR,
				INTERNAL_SERVER_ERROR.value(),
				ZonedDateTime.now(ZoneId.of("Z"))
		);

		return ResponseEntity.status(error.status())
				.contentType(APPLICATION_JSON)
				.body(error);
	}
}