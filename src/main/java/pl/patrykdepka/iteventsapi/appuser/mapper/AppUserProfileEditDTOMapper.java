package pl.patrykdepka.iteventsapi.appuser.mapper;

import pl.patrykdepka.iteventsapi.appuser.dto.AppUserProfileEditDTO;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;

import java.time.format.DateTimeFormatter;

public class AppUserProfileEditDTOMapper {

    private AppUserProfileEditDTOMapper() {
    }

    public static AppUserProfileEditDTO mapToAppUserProfileEditDTO(AppUser user) {
        return AppUserProfileEditDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .city(user.getCity())
                .bio(user.getBio())
                .build();
    }
}
