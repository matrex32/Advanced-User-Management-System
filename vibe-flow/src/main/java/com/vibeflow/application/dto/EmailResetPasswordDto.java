package com.vibeflow.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO representing the data necessary for reset password.
 * @author Denis
 */
@Getter
@Setter
@NoArgsConstructor
public class EmailResetPasswordDto {

	/**
	 * The email of the user.
	 */
	String email;
}
