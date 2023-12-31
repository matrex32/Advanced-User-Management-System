package com.vibeflow.application.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.vibeflow.application.messages.Message;

import lombok.Getter;

/**
 *  This class represents an exception that encapsulates multiple error messages and corresponding IDs
 *  @author Denis
 */
@Getter
public class MultipleVibeFlowException extends VibeFlowException {

	/**
	 * Unique ID used in serialization to verify that the sender and receiver of a serialized object maintain compatibility.
	 */
	private static final long serialVersionUID = 1L; 

	/**
	 *  A list of InputValidationError objects that represent the specific validation errors
	 */
	private final transient List<InputValidationError> fieldErrors;

	/**
	 * Constructs a MultipleOxygenAccountException with the specified details
	 * 
	 * @param fieldErrors The list of InputValidationError objects associated with this exception.
	 */
	public MultipleVibeFlowException(List<InputValidationError> fieldErrors) {
		super(Message.INPUT_VALIDATION_FAILED, HttpStatus.UNPROCESSABLE_ENTITY, InternalErrorCode.THE_CURRENT_REQUEST_VALIDATION_FAILED);
		this.fieldErrors = fieldErrors;
	}
}
