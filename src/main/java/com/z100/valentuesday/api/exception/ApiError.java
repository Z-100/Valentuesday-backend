package com.z100.valentuesday.api.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiError(String message, HttpStatus status, Integer statusCode, ZonedDateTime timestamp) {}
