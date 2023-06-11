package pl.patrykdepka.iteventsapi.appuser.dto;

import lombok.Getter;
import lombok.Setter;
import pl.patrykdepka.iteventsapi.appuser.annotation.PasswordValueMatch;

import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "{form.field.currentPassword.error.notBlank.message}")
    @Size(min = 5, max = 100, message = "{form.field.currentPassword.error.size.message}")
    private String currentPassword;
    @NotBlank(message = "{form.field.newPassword.error.notBlank.message}")
    @Size(min = 5, max = 100, message = "{form.field.newPassword.error.size.message}")
    private String newPassword;
    @NotBlank(message = "{form.field.confirmNewPassword.error.notBlank.message}")
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
