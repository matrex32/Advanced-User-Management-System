package com.vibeflow.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vibeflow.application.model.User;


/**
 * The com.vibeflow.repository package contains interfaces and classes related to the repository layer of the application.
 *
 * The UserRepository interface extends the JpaRepository interface to provide database access methods for the User entity.
 *
 * It handles CRUD operations for User objects. 
 * @author Denis
 *
 */

public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * Checks if a User entity with the provided email exists in the database.
	 *
	 * @param email The email to check for in the database.
	 * @return A boolean value representing whether a User with the provided email exists.
	 */
	boolean existsByEmail(String email);

	/**
	 * Finds a User entity in the database by the provided email.
	 * @param email The email of the User entity to find in the database.
	 * @return A User entity that matches the provided email, or null if no matching User entity is found.
	 */
	public User findByEmail(String email);
	
	/**
	 * Finds a User entity in the database by status
	 * @param status The status of the User entity to find in the database
	 * @return A User entity that matches the provided status
	 */
	public List<User> findByStatus(String status);
	
	/**
	 * Retrieves a User entity from the database by the provided ID.
	 * 
	 * @param id The ID of the User entity to retrieve from the database.
	 * @return A User entity with the provided ID, or null if no such User entity exists.
	 */
	public User findById(int id);
}

