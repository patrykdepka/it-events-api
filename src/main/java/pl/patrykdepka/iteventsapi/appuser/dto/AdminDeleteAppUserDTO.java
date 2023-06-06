package pl.patrykdepka.iteventsapi.appuser.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AdminDeleteAppUserDTO {
    private Long id;
    @NotNull(message = "{form.field.adminPassword.error.notNull.message}")
    @NotEmpty(message = "{form.field.adminPassword.error.notEmpty.message}")
    @Size(min = 5, max = 100, message = "{form.field.adminPassword.error.size.message}")
    private String adminPassword;

    public AdminDeleteAppUserDTO(Long id) {
        this.id = id;
    }

    public AdminDeleteAppUserDTO(Long id, String adminPassword) {
        this.id = id;
        this.adminPassword = adminPassword;
    }
}
