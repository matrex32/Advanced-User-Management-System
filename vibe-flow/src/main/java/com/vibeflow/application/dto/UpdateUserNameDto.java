package com.vibeflow.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a Data Transfer Object for updating the user's name.
 * @author Denis
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserNameDto {
	
	/**
	 * The new name for the user
	 */
	@NotBlank
	private String name;
}
