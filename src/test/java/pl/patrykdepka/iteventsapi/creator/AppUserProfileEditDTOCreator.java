package pl.patrykdepka.iteventsapi.creator;

import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;

import java.time.format.DateTimeFormatter;

public class AppUserProfileEditDTOCreator {

    public static AppUserProfileEditDTO create(AppUser user, String city, String bio) {
        return AppUserProfileEditDTO.builder()
//                .profileImage(ProfileImageCreator.createNewProfileImageFile())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .city(city)
                .bio(bio)
                .build();
    }
}
