package pl.patrykdepka.iteventsapi.appuser.dto;

import lombok.Getter;
import lombok.Setter;
import pl.patrykdepka.iteventsapi.appuser.annotation.PasswordValueMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "password",
                fieldMatch = "confirmPassword"
        )
})
public class AppUserRegistrationDTO {
    @NotNull(message = "{form.field.firstName.error.notNull.message}")
    @NotEmpty(message = "{form.field.firstName.error.notEmpty.message}")
    @Size(min = 2, max = 50, message = "{form.field.firstName.error.size.message}")
    private String firstName;
    @NotNull(message = "{form.field.lastName.error.notNull.message}")
    @NotEmpty(message = "{form.field.lastName.error.notEmpty.message}")
    @Size(min = 2, max = 50, message = "{form.field.lastName.error.size.message}")
    private String lastName;
    @NotNull(message = "{form.field.dateOfBirth.error.notNull.message}")
    @NotEmpty(message = "{form.field.dateOfBirth.error.notEmpty.message}")
    private String dateOfBirth;
    @NotNull(message = "{form.field.email.error.notNull.message}")
    @NotEmpty(message = "{form.field.email.error.notEmpty.message}")
    @Email(message = "{form.field.email.error.incorrectEmail.message}")
    private String email;
    @NotNull(message = "{form.field.password.error.notNull.message}")
    @NotEmpty(message = "{form.field.password.error.notEmpty.message}")
    @Size(min = 5, max = 100, message = "{form.field.password.error.size.message}")
    private String password;
    @NotNull(message = "{form.field.confirmPassword.error.notNull.message}")
    @NotEmpty(message = "{form.field.confirmPassword.error.notEmpty.message}")
    @Size(min = 5, max = 100, message = "{form.field.confirmPassword.error.size.message}")
    private String confirmPassword;

    public static AppUserRegistrationDTOBuilder builder() {
        return new AppUserRegistrationDTOBuilder();
    }

    public static class AppUserRegistrationDTOBuilder {
        private String firstName;
        private String lastName;
        private String dateOfBirth;
        private String email;
        private String password;
        private String confirmPassword;

        public AppUserRegistrationDTOBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AppUserRegistrationDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AppUserRegistrationDTOBuilder dateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public AppUserRegistrationDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AppUserRegistrationDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AppUserRegistrationDTOBuilder confirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }

        public AppUserRegistrationDTO build() {
            AppUserRegistrationDTO newUserData = new AppUserRegistrationDTO();
            newUserData.setFirstName(firstName);
            newUserData.setLastName(lastName);
            newUserData.setDateOfBirth(dateOfBirth);
            newUserData.setEmail(email);
            newUserData.setPassword(password);
            newUserData.setConfirmPassword(confirmPassword);
            return newUserData;
        }
    }
}
