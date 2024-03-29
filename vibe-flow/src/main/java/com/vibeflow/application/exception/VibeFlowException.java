package com.vibeflow.application.exception;

import org.springframework.http.HttpStatus;

import com.vibeflow.application.messages.Message;

import lombok.Getter;
/**
 * This class is used for handling custom exceptions specific to the VibeFlow Account
 * @author Denis
 */
@Getter
public class VibeFlowException extends RuntimeException{
	
	/**
	 * Unique ID used in serialization to verify that the sender and receiver of a serialized object maintain compatibility.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The HttpStatus that is to be returned. It represents the status of the HTTP response
	 */
	private final HttpStatus status;
	
	/**
	 * The id of the message that is to be returned. It is used to identify the message.
	 */
	private final String messageId;
	
	/**
	 * The error code that is to be returned. It provides more details about the specific error.
	 */
	private final InternalErrorCode errorCode;
	
	/**
	 * Constructs a new VibeFlowException with the specified detail message, HTTP status, and error code.
	 * 
	 * @param message the detail message of the error
	 * @param status the status of the HTTP response
	 * @param errorCode the internal error code
	 */
	public VibeFlowException(Message message, HttpStatus status, InternalErrorCode errorCode) {
		super(message.getMessage());
		this.status = status;
		this.messageId = message.getId();
		this.errorCode = errorCode;
	}
}