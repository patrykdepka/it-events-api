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
public class AdminAppUserPasswordEditDTO {
    private Long id;
    @NotBlank(message = "{form.field.adminPassword.error.notBlank.message}")
    @Size(min = 5, max = 100, message = "{form.field.adminPassword.error.size.message}")
    private String adminPassword;
    @NotBlank(message = "{form.field.newPassword.error.notBlank.message}")
    @Size(min = 5, max = 100, message = "{form.field.newPassword.error.size.message}")
    private String newPassword;
    @NotBlank(message = "{form.field.confirmNewPassword.error.notBlank.message}")
    @Size(min = 5, max = 100, message = "{form.field.confirmNewPassword.error.size.message}")
    private String confirmNewPassword;

    public AdminAppUserPasswordEditDTO(Long id) {
        this.id = id;
    }

    public AdminAppUserPasswordEditDTO(Long id, String adminPassword, String newPassword, String confirmNewPassword) {
        this.id = id;
        this.adminPassword = adminPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }
}
