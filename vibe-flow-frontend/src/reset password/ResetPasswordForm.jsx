import "../css/GlobalStyle.css";

import React from "react";

import { useState } from "react";

import { Grid, TextField, Button, Typography, LinearProgress, Card, CardHeader, CardContent } from "@mui/material";
import AppHeader from "../shared/AppHeader.jsx";

function ResetPasswordForm() {

    const [newPassword, setNewPassword] = useState('');

    const [confirmNewPassword, setConfirmNewPassword] = useState('');

    const [passwordError, setPasswordError] = useState('');

    const [confirmPasswordError, setConfirmPasswordError] = useState('');

    const [isLoadingActive, setisLoadingActive] = useState(false);

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
        let isConfirmPasswordValid = confirmNewPassword.trim() !== '' && password === confirmPassword;

        // Set an error message for each field if it is invalid.
        setPasswordError(isPasswordValid ? '' : 'Password must be at least 8 characters.');
        setConfirmPasswordError(isConfirmPasswordValid ? '' : 'Passwords do not match.');

        return isPasswordValid && isConfirmPasswordValid;
    }

    const handleClickButton = (event) => {
        // Validate the input fields before proceeding with registration.
        if (!validateInputs()) {
            event.preventDefault();
        }

        if (newPassword.trim() !== '') {
            setisLoadingActive(true);
        }
        // Create the new password.
        const password = {
            password: password.trim(),
        };
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
                                <Button onClick={handleClickButton} variant="contained" disabled={isLoadingActive}>
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