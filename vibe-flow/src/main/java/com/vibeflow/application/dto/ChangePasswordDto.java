package com.vibeflow.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  Data Transfer Object representing the details required to change a user's password.
 *  @author Denis
 */
@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordDto {
	
	/**
	 * The desired new password for the user.
	 */
	@Size(min = 8)
	private String newPassword;
	
	/**
	 * The current password of the user, used for verification purposes.
	 */
	@NotBlank
	private String oldPassword;

}
