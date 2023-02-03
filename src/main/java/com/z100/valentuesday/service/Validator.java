package com.z100.valentuesday.service;

import com.z100.valentuesday.api.exception.ApiException;

import java.util.Collection;

public class Validator {

	public static <INPUT> void validate(INPUT input) {
		if (input == null) {
			throw new ApiException("Input was null");
		}
	}

	public static <INPUT extends Collection<?>> void validate(INPUT input) {
		if (input == null || input.isEmpty()) {
			throw new ApiException("Input was null");
		}
	}
}
