package com.vibeflow.application.messages;

/**
 * Enum containing the email messages
 * @author Denis
 *
 */
public enum EmailMessage {

	/**
	 * Represents an error that occurs when the system is unable to find the specified email template
	 */
	EMAIL_TEMPLATE_NOT_FOUND("EMAIL_TEMPLATE_NOT_FOUND", "Email template was not found."),
	
	/**
	 * Represents an error that occurs when the system is unable to determine the type of an email
	 */
	EMAIL_TYPE_NOT_FOUND("EMAIL_TYPE_NOT_FOUND", "Email type was not found."),
	
	/**
	 * Represents an error that occurs when the system is unable to locate the properties for an email
	 */
	EMAIL_PROPERTIES_NOT_FOUND("EMAIL_PROPERTIES_NOT_FOUND", "Email properties were not found."),

	/**
	 *  Represents an error that occurs when there is a failure in processing the email template.
	 */
	EMAIL_TEMPLATE_PROCESSING_FAILED("EMAIL_TEMPLATE_PROCESSING_FAILED", "Email template processing failed.");



	/**
	 * Unique identifier corresponding to each type of message.
	 */
	private final String id;

	/**
	 * The error message associated with each unique identifier
	 */
	private final String message;

	/**
	 * Constructs a new instance of Messages with the specified identifier and message
	 * @param id the unique identifier for the message
	 * @param message the error message associated with the identifier
	 */
	private EmailMessage(String id, String message) {
		this.id = id;
		this.message = message;
	}

	/**
	 * Return the unique identifier of the message.
	 * @return the identifier
	 */
	public String getId() {
		return id;
	}
	/**
	 * Returns the error message associated with the identifier of the message
	 * @return the error message
	 */
	public String getMessage() {
		return message;
	}


}
