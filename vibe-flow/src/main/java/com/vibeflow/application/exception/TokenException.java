package com.vibeflow.application.exception;

import org.springframework.http.HttpStatus;

import com.vibeflow.application.messages.Message;

/**
 * This exception class represents token-related errors in the application.
 * 
 * @author Denis
 *
 */
public class TokenException extends VibeFlowException {
	
	/**
	 * Unique ID used in serialization to verify that the sender and receiver of a serialized object maintain compatibility.
	 */
	private static final long serialVersionUID = 1L; 
	
	/**
	 * Constructs a new TokenException with the specified URL anchor.
	 * 
	 * @param message The detail message of the error.
     * @param status The status of the HTTP response.
     * @param errorCode The internal error code.
	 */
	public TokenException(Message message, HttpStatus status, InternalErrorCode errorCode) {
		super(message, status, errorCode);
	}
}
