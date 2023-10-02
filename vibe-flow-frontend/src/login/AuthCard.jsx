import React, { useState } from 'react';
import { Grid, Card, CardHeader, CardContent, Typography, Link } from '@mui/material';
import LoginForm from './LoginForm.jsx';
import RegistrationForm from './RegistrationForm.jsx';
import EmailForgotPasswordForm from './EmailForgotPasswordForm.jsx';

/**
 * Component responsible for rendering the authentication card containing login and registration forms.
 * 
 * @returns {JSX.Element} The JSX representation of the AuthCard component.
 */
function AuthCard() {
    // State variable for managing whether to show login or registration form
    const [showLoginForm, setShowLoginForm] = useState(true);

    const [showForgotPasswordForm, setShowForgotPasswordForm] = useState(false);


    /**
    * Toggles between login and registration forms and remove hash.
    */
    const toggleForm = () => {
        if (window.location.hash) {
            history.replaceState({}, document.title, window.location.pathname);
        }
        setShowLoginForm(!showLoginForm);
    };

    const toggleForgotPasswordForm = () => {
        setShowForgotPasswordForm(!showForgotPasswordForm);
        setShowLoginForm(true);
    };

    return (
        // Main container for the authentication card
        <Grid container justifyContent="center" alignItems="center">
            <Grid item xs={12} md={9} lg={5} xl={5} style={{ marginTop: 50 }}>
                {/* Card for containing login/registration content */}
                <Card>
                    {/* Header for displaying title and the message from subheader */}
                    <CardHeader
                        title={showForgotPasswordForm ? 'Forgotten password' : (showLoginForm ? 'Log In' : 'Create New Account')}

                        subheader={
                            <Grid container alignItems="center" spacing={1}>
                                {/* Subheader text */}
                                <Grid item>
                                    <Typography>
                                        {showForgotPasswordForm ? ' ' : (showLoginForm ? "Don't have an Vibe Flow account?" : 'Already a member?')}
                                    </Typography>
                                </Grid>
                                {/* Link to toggle between login and registration forms */}
                                <Grid item>
                                    <Link
                                        component="button"
                                        underline='none'
                                        onClick={(e) => {
                                            showForgotPasswordForm ? toggleForgotPasswordForm() : toggleForm();
                                        }}
                                    >
                                        {showForgotPasswordForm ? ' Back to Log In' : (showLoginForm ? ' Create an account' : ' Log In')}
                                    </Link>

                                </Grid>
                            </Grid>
                        }
                    />
                    {/* Render either the login or registration form based on the state */}
                    <CardContent>
                        {showLoginForm
                            ? (showForgotPasswordForm
                                ? <EmailForgotPasswordForm toggleForgotPasswordForm={toggleForgotPasswordForm} />
                                : <LoginForm toggleForm={toggleForm} toggleForgotPasswordForm={toggleForgotPasswordForm} />)
                            : <RegistrationForm toggleForm={toggleForm} />}
                    </CardContent>
                </Card>
            </Grid>
        </Grid>
    );
}

export default AuthCard;