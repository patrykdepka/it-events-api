package pl.patrykdepka.iteventsapi.appuser.domain.dto;

import lombok.Getter;
import lombok.Setter;
import pl.patrykdepka.iteventsapi.appuser.domain.PasswordValueMatch;
import pl.patrykdepka.iteventsapi.appuser.domain.UniqueEmail;
import pl.patrykdepka.iteventsapi.core.DateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "{form.field.firstName.error.notBlank.message}")
    @Size(min = 2, max = 50, message = "{form.field.firstName.error.size.message}")
    private String firstName;
    @NotBlank(message = "{form.field.lastName.error.notBlank.message}")
    @Size(min = 2, max = 50, message = "{form.field.lastName.error.size.message}")
    private String lastName;
    @NotNull(message = "{form.field.dateOfBirth.error.notNull.message}")
    @DateTime(iso = DateTime.ISO.DATE)
    private String dateOfBirth;
    @NotBlank(message = "{form.field.email.error.notBlank.message}")
    @Email(message = "{form.field.email.error.incorrectEmail.message}")
    @UniqueEmail(message = "{form.field.email.error.emailIsInUse.message}")
    private String email;
    @NotBlank(message = "{form.field.password.error.notBlank.message}")
    @Size(min = 5, max = 100, message = "{form.field.password.error.size.message}")
    private String password;
    @NotBlank(message = "{form.field.confirmPassword.error.notBlank.message}")
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
