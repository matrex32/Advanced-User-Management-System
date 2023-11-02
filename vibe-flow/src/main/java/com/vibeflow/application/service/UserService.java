package com.vibeflow.application.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vibeflow.application.config.VibeFlowPropertiesConfig;
import com.vibeflow.application.dto.ChangePasswordDto;
import com.vibeflow.application.dto.DeleteUserDto;
import com.vibeflow.application.dto.EmailResetPasswordDto;
import com.vibeflow.application.dto.ResetPasswordDto;
import com.vibeflow.application.dto.UpdateUserNameDto;
import com.vibeflow.application.events.RegistrationEvent;
import com.vibeflow.application.exception.InternalErrorCode;
import com.vibeflow.application.exception.VibeFlowException;
import com.vibeflow.application.exception.UserNotAuthenticatedException;
import com.vibeflow.application.messages.Message;
import com.vibeflow.application.model.User;
import com.vibeflow.application.model.UserStatus;
import com.vibeflow.application.repository.UserRepository;
import com.vibeflow.application.type.TokenClaim;
import com.vibeflow.application.utility.DateUtility;
import com.vibeflow.application.events.ResetPasswordEvent;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;

/**
 *  Service class for user-related operations.
 *  
 *  @author Denis
 */
@Service
@AllArgsConstructor
public class UserService {
	private final ApplicationEventPublisher eventPublisher;
	
	private static final long MILIS_IN_DAY = 24L * 60L * 60L * 1000L;
	
	/**
	 * Instance of UserRepository to interact with the database.
	 */
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Instance of BCryptPasswordEncoder used for encoding the password
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private VibeFlowPropertiesConfig vibeFlowProperties;
	
	/**
	 * Register a new user in the system.
	 * 
	 * @param newUser The new User entity to be registered.
	 * @return The registered User entity
	 * @throws OxygenAccountException If a user with the same email already exists.
	 */
	public com.vibeflow.application.model.User registerUser(User newUser) {
				
		if(userRepository.existsByEmail(newUser.getEmail())) {
            throw new VibeFlowException(Message.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT, InternalErrorCode.EMAIL_ALREADY_EXISTS);
        }
		
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		newUser.setRegistrationDate(DateUtility.getCurrentUTCTimestamp());
		newUser.setStatus(UserStatus.NEW.getStatus());
		
		newUser = userRepository.save(newUser);
		
		eventPublisher.publishEvent(new RegistrationEvent(this, newUser));
		
		return newUser;
	}
	
	
	/**
	 *  Retrieves a User entity based on the provided email from the database.
	 *  
	 * @param email The email of the User entity to retrieve.
	 * @return A User entity that matches the provided email, or null if no matching User entity is found.
	 */
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	/**
	 * Saves or updates the given user entity in the repository.
	 * 
	 * @param user the user entity to be saved or updated.
	 * @return the saved or updated user entity.
	 */
	public User updateUser(User user) {
		return userRepository.save(user);
	}
	
	/**
	 * Retrieves the currently authenticated user from the security context.
	 * 
	 * @return The authenticated User entity.
	 */
	public User getCurrentUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User userPrincipal) {
		        return userRepository.findByEmail(userPrincipal.getUsername());
		}
		
		throw new UserNotAuthenticatedException();
	}
	
	/**
     * Updates the name of the user identified by the given email.
     * 
     * @param email the email address used to identify the user.
     * @param newName the new name to be set for the user.
     * @return the updated user entity.
     */
	public User updateCurrentUserName( UpdateUserNameDto newName) {
		User currentUser = getCurrentUser();
	    currentUser.setName(newName.getName());
	    userRepository.save(currentUser);
	    return currentUser;
	}
	
	/**
	 * Updates the password of the currently authenticated user after validating the input data.
	 * 
	 * @param changePasswordDto Data transfer object containing details about the old and new passwords.
	 * @return A UserDto representation of the user after the password has been updated.
	 */
	public User updateCurrentUserPassword(ChangePasswordDto changePasswordDto) {
		User currentUser = getCurrentUser();
		
		 if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), currentUser.getPassword())) {
			throw new VibeFlowException(Message.INCORRECT_PASSWORD, HttpStatus.FORBIDDEN, InternalErrorCode.INCORRECT_PASSWORD);
			
		} else if (changePasswordDto.getOldPassword().equals(changePasswordDto.getNewPassword())) {
			throw new VibeFlowException(Message.PASSWORD_SAME_AS_OLD, HttpStatus.FORBIDDEN, InternalErrorCode.PASSWORD_SAME_AS_OLD);
		}
		 currentUser.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
		 
		 return userRepository.save(currentUser);
	}
	
	/**
	 * Deletes the currently authenticated user by updating their status to "deleted" and setting their deletion date to the current timestamp.
	 * 
	 * @param deleteUserDto Data transfer object containing the password of the user to be deleted.
	 * @return The updated user entity with the "deleted" status and the current timestamp as the deletion date.
	 */
	public User deleteUser(DeleteUserDto deleteUserDto) {
        User currentUser = getCurrentUser();

        if (!passwordEncoder.matches(deleteUserDto.getPassword(), currentUser.getPassword())) {
            throw new VibeFlowException(Message.INCORRECT_PASSWORD, HttpStatus.FORBIDDEN, InternalErrorCode.INCORRECT_PASSWORD);
        }
        
        currentUser.setStatus(UserStatus.DELETED.getStatus());
        
        currentUser.setDeletionDate(DateUtility.getCurrentUTCTimestamp());
        
        return userRepository.save(currentUser);
    }
	
	/**
	 * Recovers the currently authenticated user by updating their status to "active" and setting their deletion date to null.
	 * 
	 * @return The updated user entity with the "active" status and set null the deletion date.
	 */
	public User recoverUser() {
        User currentUser = getCurrentUser();

        currentUser.setStatus(UserStatus.ACTIVE.getStatus());
        currentUser.setDeletionDate(null);
        
        return userRepository.save(currentUser);
    }
	
	/**
	 * This method is responsible for confirming the user registration through a given token.
	 * 
	 * @param token A String representing the token used for confirming user registration.
	 * @return User object representing the confirmed user with updated status.
	 * @throws  OxygenAccountException if the token is invalid, expired, or the user is already active
	 */
	public User confirmUserRegistration(String token) {
		Claims claims;

		try {
			claims = jwtService.parseToken(token);
		} catch (Exception e) {
			throw new VibeFlowException(Message.INVALID_TOKEN, HttpStatus.BAD_REQUEST, InternalErrorCode.INVALID_TOKEN);
		}

		Integer userId = claims.get(TokenClaim.USER_ID.getName(), Integer.class);
		Date creationDate = claims.get(TokenClaim.CREATION_DATE.getName(), Date.class);

		if (userId == null || creationDate == null) {
			throw new VibeFlowException(Message.INVALID_TOKEN, HttpStatus.BAD_REQUEST, InternalErrorCode.INVALID_TOKEN);
		}

		Timestamp currentDate = DateUtility.getCurrentUTCTimestamp();
		long differenceDays = Math.abs(currentDate.getTime() - creationDate.getTime())/MILIS_IN_DAY;

		if(differenceDays >= vibeFlowProperties.getDaysForEmailConfirmation()) {
			throw new VibeFlowException(Message.TOKEN_EXPIRED, HttpStatus.GONE, InternalErrorCode.TOKEN_EXPIRED);
		}

		User user = userRepository.findById((int) userId);

		if (UserStatus.ACTIVE.getStatus().equals(user.getStatus())) {
			throw new VibeFlowException(Message.USER_ALREADY_CONFIRMED, HttpStatus.GONE, InternalErrorCode.USER_ALREADY_CONFIRMED);
    	} 
        
        user.setStatus(UserStatus.ACTIVE.getStatus());
        
        return userRepository.save(user);
	}
	
	/**
	 * Method used to initiates a password reset process
	 * 
	 * @param email email the email address of the user for whom the password reset is to be initiated.
	 * @return the {@code User} object associated with the provided email.
	 * @throws VibeFlowException if no user is found associated with the given email.
	 */
	public User emailResetPassword(EmailResetPasswordDto emailDto) {
		String userEmail = emailDto.getEmail();
		User currentUser = userRepository.findByEmail(userEmail);
		
		if(currentUser == null) {
			throw new VibeFlowException(Message.USER_DOESNT_EXIST, HttpStatus.NOT_FOUND, InternalErrorCode.USER_DOESNT_EXIST);
		}
		eventPublisher.publishEvent(new ResetPasswordEvent(this, currentUser));
		
		return currentUser;
	}
	
	/**
	 * Verifies the provided token used for resetting a password.
	 * 
	 * @param token The JWT token to be verified.
	 * @return claims The claims extracted from the valid token.
	 */
	public Claims verifyResetPasswordToken (String token) {
		Claims claims;
		
		try {
			claims = jwtService.parseToken(token);
		} catch (Exception e) {
			throw new VibeFlowException(Message.INVALID_TOKEN, HttpStatus.BAD_REQUEST, InternalErrorCode.INVALID_TOKEN);
		} 
		
		Integer userId = claims.get(TokenClaim.USER_ID.getName(), Integer.class);

		if (userId == null) {
			throw new VibeFlowException(Message.INVALID_TOKEN, HttpStatus.BAD_REQUEST, InternalErrorCode.INVALID_TOKEN);
		}
		

		Timestamp currentDate = DateUtility.getCurrentUTCTimestamp();
		Timestamp tokenIssueDate = new Timestamp(claims.getIssuedAt().getTime());

		long differenceMilliseconds = currentDate.getTime() - tokenIssueDate.getTime();
		long differenceDays = TimeUnit.MILLISECONDS.toDays(differenceMilliseconds);

		if(differenceDays >= vibeFlowProperties.getDaysForResetPassword()) {
			throw new VibeFlowException(Message.TOKEN_EXPIRED, HttpStatus.GONE, InternalErrorCode.TOKEN_EXPIRED);
		}
		
		return claims;
	}
	
	/**
	 * Resets the password of a user based on the provided JWT token and new password details.
	 * 
	 * @param token The JWT token used to verify the user's identity and the validity of the reset request.
	 * @param resetPasswordDto Data Transfer Object containing the new password details.
	 * @return The updated {@code User} object after the password reset.
	 */
	public User resetUserPassword(ResetPasswordDto resetPasswordDto) {
		
		Claims claims = verifyResetPasswordToken(resetPasswordDto.getToken());
		
		Integer userId = claims.get(TokenClaim.USER_ID.getName(), Integer.class);
		
		User user = userRepository.findById((int) userId);
		user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
		
		return userRepository.save(user);
	}
}
