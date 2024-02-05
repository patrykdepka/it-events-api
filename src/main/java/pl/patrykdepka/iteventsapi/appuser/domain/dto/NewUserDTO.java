package pl.patrykdepka.iteventsapi.appuser.domain.dto;

import lombok.Builder;
import lombok.Value;
import pl.patrykdepka.iteventsapi.appuser.domain.PasswordValueMatch;
import pl.patrykdepka.iteventsapi.appuser.domain.UniqueEmail;
import pl.patrykdepka.iteventsapi.core.DateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "password",
                fieldMatch = "confirmPassword"
        )
})
@Builder
public class NewUserDTO {
    @NotBlank(message = "{form.field.firstName.error.notBlank.message}")
    @Size(min = 2, max = 50, message = "{form.field.firstName.error.size.message}")
    String firstName;
    @NotBlank(message = "{form.field.lastName.error.notBlank.message}")
    @Size(min = 2, max = 50, message = "{form.field.lastName.error.size.message}")
    String lastName;
    @NotBlank(message = "{form.field.dateOfBirth.error.notBlank.message}")
    @DateTime(iso = DateTime.ISO.DATE)
    String dateOfBirth;
    @NotBlank(message = "{form.field.email.error.notBlank.message}")
    @Email(message = "{form.field.email.error.incorrectEmail.message}")
    @UniqueEmail(message = "{form.field.email.error.emailIsInUse.message}")
    String email;
    @NotBlank(message = "{form.field.password.error.notBlank.message}")
    @Size(min = 5, max = 100, message = "{form.field.password.error.size.message}")
    String password;
    @NotBlank(message = "{form.field.confirmPassword.error.notBlank.message}")
    @Size(min = 5, max = 100, message = "{form.field.confirmPassword.error.size.message}")
    String confirmPassword;
}
