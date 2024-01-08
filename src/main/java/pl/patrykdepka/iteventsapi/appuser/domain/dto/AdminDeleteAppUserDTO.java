package pl.patrykdepka.iteventsapi.appuser.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AdminDeleteAppUserDTO {
    private Long id;
    @NotBlank(message = "{form.field.adminPassword.error.notBlank.message}")
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
