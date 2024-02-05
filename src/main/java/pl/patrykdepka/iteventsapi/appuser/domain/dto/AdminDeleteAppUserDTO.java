package pl.patrykdepka.iteventsapi.appuser.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AdminDeleteAppUserDTO {
    @NotBlank(message = "{form.field.adminPassword.error.notBlank.message}")
    @Size(min = 5, max = 100, message = "{form.field.adminPassword.error.size.message}")
    String adminPassword;
}
