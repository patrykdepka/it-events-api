package pl.patrykdepka.iteventsapi.appuser.domain.dto;

import lombok.Value;
import pl.patrykdepka.iteventsapi.appuser.domain.Role;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
public class AdminAppUserAccountEditDTO {
    Long id;
    @NotNull(message = "{form.field.someField.error.notNull.message}")
    boolean enabled;
    @NotNull(message = "{form.field.someField.error.notNull.message}")
    boolean accountNonLocked;
    @NotNull(message = "{form.field.roles.error.notNull.message}")
    @NotEmpty(message = "{form.field.roles.error.notEmpty.message}")
    List<Role> roles;

    public static AdminAppUserAccountEditDTOBuilder builder() {
        return new AdminAppUserAccountEditDTOBuilder();
    }

    public static class AdminAppUserAccountEditDTOBuilder {
        private Long id;
        private boolean enabled;
        private boolean accountNonLocked;
        private List<Role> roles;

        public AdminAppUserAccountEditDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AdminAppUserAccountEditDTOBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public AdminAppUserAccountEditDTOBuilder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public AdminAppUserAccountEditDTOBuilder roles(List<Role> roles) {
            this.roles = roles;
            return this;
        }

        public AdminAppUserAccountEditDTO build() {
            return new AdminAppUserAccountEditDTO(
                    id,
                    enabled,
                    accountNonLocked,
                    roles
            );
        }
    }
}
