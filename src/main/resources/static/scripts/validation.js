function validateLoginForm() {
    const usernameValue = document.loginForm.username.value;
    const isNotUsernameError = checkIfNotEmpty('username', usernameValue);
    const passwordValue = document.loginForm.password.value;
    const isNotPasswordError = checkIfNotEmpty('password', passwordValue);

    return isNotUsernameError && isNotPasswordError;
}

function validateRegistrationForm() {
    const firstNameValue = document.registrationForm.firstName.value;
    const isNotFirstNameError = checkIfNotEmpty('firstName', firstNameValue);
    const lastNameValue = document.registrationForm.lastName.value;
    const isNotLastNameError = checkIfNotEmpty('lastName', lastNameValue);
    const emailValue = document.registrationForm.email.value;
    const isNotEmailError = checkIfNotEmpty('email', emailValue);
    const passwordValue = document.registrationForm.password.value;
    const isNotPasswordError = checkIfNotEmpty('password', passwordValue);
    const confirmPasswordValue = document.registrationForm.confirmPassword.value;
    const isNotConfirmPasswordError = checkIfNotEmpty('confirmPassword', confirmPasswordValue);

    return isNotFirstNameError && isNotLastNameError && isNotEmailError && isNotPasswordError && isNotConfirmPasswordError;
}

function checkIfNotEmpty(fieldName, fieldValue) {
    if (!fieldValue) {
        document.getElementById(`${fieldName}`).className='form-control is-invalid';
        if (document.getElementById(`${fieldName}ErrorMessage`).style.display === 'none') { // check if error message is not displaying
            document.getElementById(`${fieldName}ErrorMessage`).style.display = ''; // display error message
            document.getElementById(`${fieldName}`).value = '';
        }

        return false;
    }

    document.getElementById(`${fieldName}`).className='form-control';
    if (document.getElementById(`${fieldName}ErrorMessage`).style.display === '') {
        document.getElementById(`${fieldName}ErrorMessage`).style.display = 'none';
    }

    return true;
}
