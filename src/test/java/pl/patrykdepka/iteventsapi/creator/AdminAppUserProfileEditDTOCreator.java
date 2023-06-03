package pl.patrykdepka.iteventsapi.creator;

import pl.patrykdepka.iteventsapi.appuser.dto.AdminAppUserProfileEditDTO;

public class AdminAppUserProfileEditDTOCreator {

    public static AdminAppUserProfileEditDTO create() {
        return AdminAppUserProfileEditDTO.builder()
                .firstName("Marcin")
                .lastName("Nowak")
                .dateOfBirth("1997-10-06")
                .city("Rzeszów")
                .bio("Cześć! Nazywam się Marcin Nowak i jestem z Rzeszowa.")
                .build();
    }
}
