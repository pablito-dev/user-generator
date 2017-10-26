package com.pablito.generator.exception;

/**
 * Created by Paweł Nowak on 10/26/17.
 */
public class GoogleRequestDeniedException extends RuntimeException {
	public GoogleRequestDeniedException() {
		super("Invalid or expired API key");
	}
}
