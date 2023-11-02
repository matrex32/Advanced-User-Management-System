package com.vibeflow.application.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object representing the details required to reset a user's password. 
 * 
 * @author Denis
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class ResetPasswordDto {

	/**
	 * The new password of the user
	 */
	@Size(min = 8)
	String newPassword;
	
	/**
	 * The token of the user
	 */
	String token;
}
