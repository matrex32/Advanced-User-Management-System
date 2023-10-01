package com.vibeflow.application.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for setting up the password encoder.
 * 
 * @author Denis
 */
@Configuration
public class PasswordEncoderConfig {

	/**
	 * This password encoder use BCryptoPasswordEncoder to encode the password
	 * @return a BCryptoPasswordEncoder
	 */
    @Bean("encoder")
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
