package pl.patrykdepka.iteventsapi.appuser.domain.mapper;

import org.springframework.data.domain.Page;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserTableDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;

public class AdminAppUserTableDTOMapper {

    private AdminAppUserTableDTOMapper() {
    }

    public static Page<AdminAppUserTableDTO> mapToAdminAppUserTableDTOs(Page<AppUser> users) {
        return users.map(AdminAppUserTableDTOMapper::mapToAdminAppUserTableDTO);
    }

    private static AdminAppUserTableDTO mapToAdminAppUserTableDTO(AppUser user) {
        return AdminAppUserTableDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .roles(user.getRoles())
                .build();
    }
}
