import React, { useState } from 'react';
import { TextField, Button, Grid, Snackbar, Alert, Link } from '@mui/material';

function EmailForgotPasswordForm() {
    const [email, setEmail] = useState('');
    const [emailError, setEmailError] = useState('');
    const [showSnackbar, setShowSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [isSuccessSnackbar, setIsSuccessSnackbar] = useState(false);

    const handleInputChange = (event) => {
        setEmail(event.target.value);
        if (emailError !== '') {
            setEmailError('');
        }
    }

    const handleSubmit = () => {
        // Logica pentru a trimite email-ul și pentru a afișa Snackbar cu mesaj corespunzător.
    }

    const handleSnackbarClose = () => {
        setShowSnackbar(false);
    };

     /**
    * Validates the input fields for user authentication.
    * @returns {boolean} True if all input fields are valid, otherwise false.
    */
     const validateInputs = () => {
        let isEmailValid = email.trim() !== '';

        // Set an error message for each field if it is invalid.
        setEmailError(isEmailValid ? '' : 'Empty email.');

        return isEmailValid;
    }

    return (
        <form>
            <Grid container spacing={3} direction="column">
                <Grid item>
                    <TextField
                        id="email-forgot-password"
                        label="Email"
                        variant="outlined"
                        value={email}
                        onChange={handleInputChange}
                        error={Boolean(emailError)}
                        helperText={emailError}
                        fullWidth
                    />
                </Grid>
                <Grid item container justifyContent="center">
                    <Button onClick={handleSubmit} variant="contained">
                        Send Reset Email
                    </Button>

                </Grid>
                {showSnackbar &&
                    <Grid item>
                        <Snackbar
                            open={showSnackbar}
                            autoHideDuration={5000}
                            onClose={handleSnackbarClose}
                            anchorOrigin={{
                                vertical: "bottom",
                                horizontal: "center"
                            }}
                        >
                            <Alert
                                elevation={6}
                                variant="filled"
                                onClose={handleSnackbarClose}
                                severity={isSuccessSnackbar ? 'success' : 'error'}
                            >
                                {snackbarMessage}
                            </Alert>
                        </Snackbar>
                    </Grid>
                }
            </Grid>
        </form>
    );
}

export default EmailForgotPasswordForm;
