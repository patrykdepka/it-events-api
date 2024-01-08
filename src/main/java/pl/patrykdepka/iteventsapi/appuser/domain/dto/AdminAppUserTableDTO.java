package pl.patrykdepka.iteventsapi.appuser.domain.dto;

import lombok.Getter;
import lombok.Setter;
import pl.patrykdepka.iteventsapi.appuser.domain.Role;

import java.util.List;

@Getter
@Setter
public class AdminAppUserTableDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private boolean accountNonLocked;
    private List<Role> roles;

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
            AdminAppUserTableDTO userTable = new AdminAppUserTableDTO();
            userTable.setId(id);
            userTable.setFirstName(firstName);
            userTable.setLastName(lastName);
            userTable.setEmail(email);
            userTable.setEnabled(enabled);
            userTable.setAccountNonLocked(accountNonLocked);
            userTable.setRoles(roles);
            return userTable;
        }
    }
}
