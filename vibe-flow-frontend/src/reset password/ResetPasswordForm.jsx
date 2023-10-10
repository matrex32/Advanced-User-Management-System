import "../css/GlobalStyle.css";

import React from "react";

import { useState, useContext } from "react";

import { Grid, TextField, Button, Typography, LinearProgress } from "@mui/material";
import AuthCard from "../login/AuthCard.jsx";
import AppHeader from "../shared/AppHeader.jsx";

function ResetPasswordForm() {

    const [newPasswod, setNewPassword] = useState('');

    const [confirmNewPassword, setConfirmNewPassword] = useState('');

    const [nameError, setNameError] = useState('');

    const [passwordError, setPasswordError] = useState('');

    const [confirmPasswordError, setConfirmPasswordError] = useState('');

    const [isLoadingActive, setisLoadingActive] = useState(false);

    const [snackbarMessage, setSnackbarMessage] = useState('');

    const [showSnackbar, setShowSnackbar] = useState(false);

    const handleInputChange = (event) => {
        const { id, value } = event.target;

        switch (id) {
            case "new-password":
                setNewPassword(value);

                if (nameError !== '') {
                    setPasswordError('');
                }
                break;

            case "confirm-new-password":
                setConfirmNewPassword(value);

                if (nameError !== '') {
                    setConfirmPasswordError('');
                }
                break;
        }
    }

    const clearPasswordFileds = (event) => {
        setNewPassword('');
        setConfirmNewPassword('');
    }

    return (
        <Grid container direction="column">
            <Grid item container maxWidth={false}>
                <AppHeader showLogoutButton={false} />
            </Grid>
        </Grid>
    );
};

export default ResetPasswordForm;