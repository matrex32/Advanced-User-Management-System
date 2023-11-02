import "../css/GlobalStyle.css";

import React from "react";

import { useState, useEffect } from "react";

import { Grid, TextField, Button, Typography, LinearProgress, Card, CardHeader, CardContent, Snackbar, Alert } from "@mui/material";
import AppHeader from "../shared/AppHeader.jsx";

function ResetPasswordForm() {

    const [newPassword, setNewPassword] = useState('');

    const [confirmNewPassword, setConfirmNewPassword] = useState('');

    const [passwordError, setPasswordError] = useState('');

    const [confirmPasswordError, setConfirmPasswordError] = useState('');

    const [isLoadingActive, setIsLoadingActive] = useState(false);

    const [snackbarMessage, setSnackbarMessage] = useState('');

    const [showSnackbar, setShowSnackbar] = useState(false);

    const [isSuccessSnackbar, setIsSuccessSnackbar] = useState(false);

    const handleInputChange = (event) => {
        const { id, value } = event.target;

        switch (id) {
            case "new-password":
                setNewPassword(value);

                if (passwordError !== '') {
                    setPasswordError('');
                }
                break;

            case "confirm-new-password":
                setConfirmNewPassword(value);

                if (confirmPasswordError !== '') {
                    setConfirmPasswordError('');
                }
                break;
        }
    }

    const clearPasswordFileds = (event) => {
        setNewPassword('');
        setConfirmNewPassword('');
    }

    const displayInputErrorsFromServer = (errorsList) => {
        errorsList.forEach(error => {
            switch (error.fieldName) {
                case "password":
                    setPasswordError(error.errorMessage);
                    break;
            }
        });
    }

    const validateInputs = () => {
        let isPasswordValid = /^.{8,}$/.test(newPassword.trim());
        let isConfirmPasswordValid = confirmNewPassword.trim() !== '' && newPassword === confirmNewPassword;

        // Set an error message for each field if it is invalid.
        setPasswordError(isPasswordValid ? '' : 'Input field is too short. Please enter a longer value.');
        setConfirmPasswordError(isConfirmPasswordValid ? '' : 'Passwords do not match.');

        return isPasswordValid && isConfirmPasswordValid;
    }

    const getTokenFromUrl = () => {
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get('token');

        if (!token) {
            setSnackbarMessage('Invalid token!');
            setShowSnackbar(true);
            return;
        }

        return token;
    }

    const handleClickButton = (event) => {
        // Validate the input fields before proceeding with registration.
        if (!validateInputs()) {
            event.preventDefault();
        }

        const token = getTokenFromUrl();
        if (!token) {
            return;
        }

        // Create the new password data
        if (newPassword.trim() !== '') {
            setIsLoadingActive(true);

            const passwordData = {
                token: token,
                password: newPassword.trim(),
            };
            sendResetPasswordRequest(passwordData);
        }
    }

    const sendResetPasswordRequest = (passwordData) => {
        setIsLoadingActive(true);

        return fetch('/api/users/reset-password', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(passwordData),
        })
            .then((response) => {
                if (response.ok) {
                    clearPasswordFileds();

                    setIsLoadingActive(false);

                    setIsSuccessSnackbar(true);
                    setSnackbarMessage('Your password has been reset.');
                    setShowSnackbar(true);
                }
               return response.json();
            })
            .then((data) => {
                setIsLoadingActive(false);

                if (data.errors) {
                    displayInputErrorsFromServer(data.errors);
                } else if (data.errorMessage) {
                    throw new Error(data.errorMessage);
                }
            })
            .catch(error => {
                setIsLoadingActive(false);

                setIsSuccessSnackbar(false);

                // If the error name is 'TypeError', it means there was a connection error.
                // Otherwise, display the error message from the error object.
                setSnackbarMessage(error.name == "TypeError" ? "The connection could not be established." : error.message);

                setShowSnackbar(true);
            });
    }

    /*
  * Handle the Snackbar close event.
  */
    const handleSnackbarClose = () => {
        setShowSnackbar(false);
    };

    return (
        <Grid container justifyContent="center" alignItems="center">
            <Grid container direction="column">
                <Grid item container maxWidth={false}>
                    <AppHeader showLogoutButton={false} />
                </Grid>
            </Grid>

            <Grid item xs={12} md={10} lg={8} xl={6} style={{ marginTop: 50 }}>

                <Card>
                    {/* Header for displaying title*/}
                    <CardHeader
                        title={"Reset Password"}
                    />

                    {/* Display loading process or fields for new password */}
                    <CardContent>
                        {/* Name input field */}
                        <Grid container spacing={3} direction="column">
                            <Grid item>
                                <TextField
                                    id="new-password"
                                    label="New password"
                                    variant="outlined"
                                    value={newPassword}
                                    onChange={handleInputChange}
                                    error={Boolean(passwordError)}
                                    helperText={passwordError}
                                    fullWidth
                                />
                            </Grid>

                            {/* Name input field */}
                            <Grid item>
                                <TextField
                                    id="confirm-new-password"
                                    label="Confirm new password"
                                    variant="outlined"
                                    value={confirmNewPassword}
                                    onChange={handleInputChange}
                                    error={Boolean(confirmPasswordError)}
                                    helperText={confirmPasswordError}
                                    fullWidth
                                />
                            </Grid>

                            {/* "Create reset password button */}
                            <Grid item container justifyContent="center">
                                <Button type="submit" onClick={handleClickButton} variant="contained" disabled={isLoadingActive} >
                                    Reset password
                                </Button>
                            </Grid>

                            {/* Conditionally render the loading indicator if isLoadingActive is true */}
                            {isLoadingActive &&
                                <Grid item>
                                    <LinearProgress />
                                </Grid>}


                            {/* Conditionally render the Snackbar for showing success or error messages if showSnackbar is true */}
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
                                </Grid>}

                        </Grid>
                    </CardContent>
                </Card>
            </Grid>
        </Grid>
    );
};

export default ResetPasswordForm;