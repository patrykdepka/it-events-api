package pl.patrykdepka.iteventsapi.appuser.domain.dto;

import lombok.Value;
import pl.patrykdepka.iteventsapi.appuser.domain.Role;

import java.util.List;

@Value
public class AdminAppUserTableDTO {
    Long id;
    String firstName;
    String lastName;
    String email;
    boolean enabled;
    boolean accountNonLocked;
    List<Role> roles;

    public static AdminAppUserTableDTOBuilder builder() {
        return new AdminAppUserTableDTOBuilder();
    }

    public static class AdminAppUserTableDTOBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private boolean enabled;
        private boolean accountNonLocked;
        private List<Role> roles;

        public AdminAppUserTableDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AdminAppUserTableDTOBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AdminAppUserTableDTOBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AdminAppUserTableDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AdminAppUserTableDTOBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public AdminAppUserTableDTOBuilder accountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public AdminAppUserTableDTOBuilder roles(List<Role> roles) {
            this.roles = roles;
            return this;
        }

        public AdminAppUserTableDTO build() {
            return new AdminAppUserTableDTO(
                    id,
                    firstName,
                    lastName,
                    email,
                    enabled,
                    accountNonLocked,
                    roles
            );
        }
    }
}
