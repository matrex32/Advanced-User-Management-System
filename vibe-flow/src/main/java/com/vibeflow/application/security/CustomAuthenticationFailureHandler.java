package com.vibeflow.application.security;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.vibeflow.application.exception.EmailNotConfirmedException;
import com.vibeflow.application.type.UrlAnchor;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The {@code CustomAuthenticationFailureHandler} class extends {@code SimpleUrlAuthenticationFailureHandler} 
 * to provide custom handling for authentication failures within the VibeFlow application.
 * 
 * @author Denis
 *
 */
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	/**
	 * Method to differentiate the failure URL based on the type of {@code AuthenticationException} received. 
	 */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof BadCredentialsException) {
            setDefaultFailureUrl(UrlAnchor.INVALID_USER.getAnchor());
        }   else if (exception instanceof EmailNotConfirmedException) {
            setDefaultFailureUrl(UrlAnchor.UNCONFIRMED_USER.getAnchor());
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
