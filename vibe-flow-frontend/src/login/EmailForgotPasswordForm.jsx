import React, { useState } from 'react';
import { TextField, Button, Grid, LinearProgress, Snackbar, Alert } from '@mui/material';

function EmailForgotPasswordForm({ toggleForgotPasswordForm }) {
    const [email, setEmail] = useState('');
    const [emailError, setEmailError] = useState('');
    const [showSnackbar, setShowSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [isSuccessSnackbar, setIsSuccessSnackbar] = useState(false);

    // State variable indicating when the form submission is in progress.
    const [isLoadingActive, setisLoadingActive] = useState(false);


    const handleInputChange = (event) => {
        setEmail(event.target.value);
        if (emailError !== '') {
            setEmailError('');
        }
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

    const clearInputFields = () => {
        setEmail('');
    }

    /**
    * Send a registration request to the server to create a new user account.
    * @param {Object} newUser - The user object with name, email, and password properties.
    * @returns {Promise} A promise that resolves with the response data from the server.
    * @throws {Error} If the response from the server contains an errorMessage.
    */
    const sendEmailResetPasswordRequest = (frgottenUser) => {
        setisLoadingActive(true);

        return fetch('api/users/email-reset-password', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(frgottenUser),
        })
            .then((response) => {
                if (response.ok) {
                    clearInputFields();

                    setisLoadingActive(false);

                    setIsSuccessSnackbar(true);
                    setSnackbarMessage('A reset email has been sent to your address.');
                    setShowSnackbar(true);
                }
                return response.json();
            })
            .then((data) => {
                setisLoadingActive(false);

                if (data.errors) {
                    displayInputErrorsFromServer(data.errors);
                } else if (data.errorMessage) {
                    throw new Error(data.errorMessage);
                }
            })
            .catch(error => {
                setisLoadingActive(false);

                setIsSuccessSnackbar(false);

                // If the error name is 'TypeError', it means there was a connection error.
                // Otherwise, display the error message from the error object.
                setSnackbarMessage(error.name == "TypeError" ? "The connection could not be established." : error.message);

                setShowSnackbar(true);
            });
    };

    const handleClickButton = (event) => {
        if (!validateInputs()) {
            event.preventDefault();
        }

        if (email.trim() !== '') {
            setisLoadingActive(true);

            const forgottenUser = {
                email: email.trim(),
            };

            sendEmailResetPasswordRequest(forgottenUser);
        }
    };

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
                <Grid item container justifyContent="center" spacing={2}>
                    <Grid item>
                        <Button
                            onClick={toggleForgotPasswordForm}
                            variant="contained"
                            sx={{ backgroundColor: 'grey', '&:hover': { backgroundColor: 'darkgrey' } }}
                        >
                            Cancel
                        </Button>
                    </Grid>

                    <Grid item>
                        <Button onClick={handleClickButton} variant="contained" disabled={isLoadingActive}>
                            Search
                        </Button>
                    </Grid>
                </Grid>

                {/* Conditionally render the loading indicator if isLoadingActive is true */}
                {isLoadingActive &&
                    <Grid item xs>
                        <LinearProgress />
                    </Grid>}

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
        </form >
    );
}

export default EmailForgotPasswordForm;
