package pl.patrykdepka.iteventsapi.appuser.domain.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class AdminDeleteAppUserDTO {
    Long id;
    @NotBlank(message = "{form.field.adminPassword.error.notBlank.message}")
    @Size(min = 5, max = 100, message = "{form.field.adminPassword.error.size.message}")
    String adminPassword;
}
