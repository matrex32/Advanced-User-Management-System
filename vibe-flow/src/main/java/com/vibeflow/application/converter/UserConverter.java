package com.vibeflow.application.converter;

import org.springframework.stereotype.Component;

import com.vibeflow.application.dto.UserDto;
import com.vibeflow.application.model.User;

/**
 * A utility class that provides methods for converting between {@link UserDto} and {@link User}
 * @author Denis
 */

@Component
public class UserConverter {
	
	/**
	 * Converts a {@link User} object to a {@link UserDto} object.
	 * 
	 * @param user the User object to be converted.
	 * @return the corresponding UserDto object with the name and email
	 */
	
	public UserDto entityToDto(User user) {
		
		UserDto userDto = new UserDto();
		
		userDto.setName(user.getName());
		userDto.setEmail(user.getEmail());
		userDto.setStatus(user.getStatus());
	    userDto.setDeletionDate(user.getDeletionDate());
		
		return userDto;
	}
	
	/**
	 * Converts a {@link UserDto} object to a {@link User} object.
	 * 
	 * @param userDto the UserDto object to be converted
	 * @return the corresponding User object with the name, email and password
	 */
	
	public User dtoToEntity(UserDto userDto) {
	
		User user = new User();
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		
		return user;
	}
}
