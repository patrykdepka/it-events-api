package pl.patrykdepka.iteventsapi.appuser.mapper;

import pl.patrykdepka.iteventsapi.appuser.dto.AdminAppUserAccountEditDTO;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;

public class AdminAppUserAccountEditDTOMapper {

    private AdminAppUserAccountEditDTOMapper() {
    }

    public static AdminAppUserAccountEditDTO mapToAdminAppUserAccountEditDTO(AppUser user) {
        return AdminAppUserAccountEditDTO.builder()
                .id(user.getId())
                .enabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .roles(user.getRoles())
                .build();
    }
}
