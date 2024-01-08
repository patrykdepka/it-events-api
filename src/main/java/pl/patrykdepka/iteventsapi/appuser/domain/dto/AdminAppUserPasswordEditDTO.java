package pl.patrykdepka.iteventsapi.appuser.domain.dto;

import lombok.Value;
import pl.patrykdepka.iteventsapi.appuser.domain.PasswordValueMatch;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "newPassword",
                fieldMatch = "confirmNewPassword"
        )
})
public class AdminAppUserPasswordEditDTO {
    Long id;
    @NotBlank(message = "{form.field.adminPassword.error.notBlank.message}")
    @Size(min = 5, max = 100, message = "{form.field.adminPassword.error.size.message}")
    String adminPassword;
    @NotBlank(message = "{form.field.newPassword.error.notBlank.message}")
    @Size(min = 5, max = 100, message = "{form.field.newPassword.error.size.message}")
    String newPassword;
    @NotBlank(message = "{form.field.confirmNewPassword.error.notBlank.message}")
    @Size(min = 5, max = 100, message = "{form.field.confirmNewPassword.error.size.message}")
    String confirmNewPassword;
}
