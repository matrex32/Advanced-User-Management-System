package com.vibeflow.application.dto;

import java.util.Map;

import com.vibeflow.application.type.EmailType;

import lombok.Getter;
import lombok.Setter;

/**
 * An object representing email information.
 * @author Denis
 */
@Getter
@Setter
public class EmailInfo {

	/**
	 * The type of the email.
	 */
	private EmailType type;

	/**
	 * The email address of the recipient.
	 */
	private String emailAddress;

	/**
	 * Data for constructing the email content.
	 */
	private Map<String, Object> emailData;
}
