package com.z100.valentuesday.service;

import com.z100.valentuesday.api.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.Collection;

public class Validator {

	public static <INPUT> void notNull(INPUT input) {
		if (input == null) {
			throw new ApiException("Input was null");
		}
	}

	public static <INPUT extends Collection<?>> void notNull(INPUT input) {
		if (input == null || input.isEmpty()) {
			throw new ApiException("Input was null");
		}
	}
}
