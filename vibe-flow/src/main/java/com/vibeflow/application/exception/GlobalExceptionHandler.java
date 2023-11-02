package com.vibeflow.application.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vibeflow.application.dto.PasswordResetResponseDto;
import com.vibeflow.application.messages.Message;
import com.vibeflow.application.type.UrlAnchor;

/**
 * This class handles exceptions globally for the application and provides central exception handling across all HTTP requests
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Method handles OxygenAccountExceptions that may be thrown during the execution of HTTP requests.
	 * @param exception the instance of OxygenAccountExceptions that has been thrown.
	 * @return ResponseEntity<ErrorResponse> this is the HTTP response containing the details of the exception that was handled.
	 */
	@ExceptionHandler(VibeFlowException.class)
	public ResponseEntity<ErrorResponse> handleVibeFlowExceptions(VibeFlowException exception) {
		ErrorResponse errorResponse = new ErrorResponse();
		
		errorResponse.setInternalErrorCode(exception.getErrorCode().getInternalErrorCode());
		errorResponse.setErrorMessage(exception.getMessage());
		errorResponse.setMessageId(exception.getMessageId());
		
		return new ResponseEntity<>(errorResponse, exception.getStatus());
	}
	
	/**
	 * Method handles MultipleOxygenAccountException that may be thrown during the execution of HTTP request
	 * @param exception the instance of MultipleOxygenAccountException that has been thrown.
	 * @return ResponseEntity<ErrorResponse> this is the HTTP response containing the details of the exception that was handled
	 */
	@ExceptionHandler(MultipleVibeFlowException.class)
	public ResponseEntity<ErrorResponse> handleMultipleVibeFlowException(MultipleVibeFlowException exception) {
		ErrorResponse errorResponse = new ErrorResponse();

		errorResponse.setInternalErrorCode(exception.getErrorCode().getInternalErrorCode());
		errorResponse.setErrors(exception.getFieldErrors());
		errorResponse.setErrorMessage(exception.getMessage());
		errorResponse.setMessageId(exception.getMessageId());
	
		return new ResponseEntity<>(errorResponse, exception.getStatus());
	}
	
	@ExceptionHandler(TokenException.class)
	@ResponseBody
	public PasswordResetResponseDto handleVibeFlowException(VibeFlowException e) {
		
		if (e.getMessageId().equals(Message.INVALID_TOKEN.getId())) {
			return new PasswordResetResponseDto(UrlAnchor.INVALID_TOKEN.getAnchor());
			
		} else if (e.getMessageId().equals(Message.TOKEN_EXPIRED.getId())) {
			return new PasswordResetResponseDto(UrlAnchor.RESET_PASSWORD_TOKEN_EXPIRED.getAnchor());
		} else {
			return new PasswordResetResponseDto(null);
		}
	}
}

