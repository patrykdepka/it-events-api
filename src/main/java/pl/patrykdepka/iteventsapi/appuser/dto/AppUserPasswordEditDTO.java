package pl.patrykdepka.iteventsapi.appuser.dto;

import lombok.Getter;
import lombok.Setter;
import pl.patrykdepka.iteventsapi.appuser.annotation.PasswordValueMatch;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "newPassword",
                fieldMatch = "confirmNewPassword"
        )
})
public class AppUserPasswordEditDTO {
    @NotNull(message = "{form.field.currentPassword.error.notNull.message}")
    @NotEmpty(message = "{form.field.currentPassword.error.notEmpty.message}")
    @Size(min = 5, max = 100, message = "{form.field.currentPassword.error.size.message}")
    private String currentPassword;
    @NotNull(message = "{form.field.newPassword.error.notNull.message}")
    @NotEmpty(message = "{form.field.newPassword.error.notEmpty.message}")
    @Size(min = 5, max = 100, message = "{form.field.newPassword.error.size.message}")
    private String newPassword;
    @NotNull(message = "{form.field.confirmNewPassword.error.notNull.message}")
    @NotEmpty(message = "{form.field.confirmNewPassword.error.notEmpty.message}")
    @Size(min = 5, max = 100, message = "{form.field.confirmNewPassword.error.size.message}")
    private String confirmNewPassword;

    public AppUserPasswordEditDTO() {
    }

    public AppUserPasswordEditDTO(String currentPassword, String newPassword, String confirmNewPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }
}
