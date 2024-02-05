package pl.patrykdepka.iteventsapi.appuser.domain.mapper;

import pl.patrykdepka.iteventsapi.appuser.domain.dto.AdminAppUserAccountEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;

public class AdminAppUserAccountDTOMapper {

    private AdminAppUserAccountDTOMapper() {
    }

    public static AdminAppUserAccountEditDTO mapToAdminAppUserAccountDTO(AppUser user) {
        return AdminAppUserAccountEditDTO.builder()
                .enabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .roles(user.getRoles())
                .build();
    }
}
