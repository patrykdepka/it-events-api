package pl.patrykdepka.iteventsapi.creator;

import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileEditDTO;
import pl.patrykdepka.iteventsapi.image.domain.dto.ImageDTO;

public class AppUserProfileEditDTOCreator {

    public static AppUserProfileEditDTO createNewProfileData(ImageDTO profileImage, String city, String bio) {
        return AppUserProfileEditDTO.builder()
                .currentProfileImageData("")
                .profileImage(profileImage)
                .firstName("Testowy")
                .lastName("Użytkownik")
                .dateOfBirth("2000-01-01")
                .city(city)
                .bio(bio)
                .build();
    }

    public static AppUserProfileEditDTO createNewProfileData(
            ImageDTO profileImage, String firstName, String lastName, String dateOfBirth, String city, String bio) {
        return AppUserProfileEditDTO.builder()
                .currentProfileImageData(null)
                .profileImage(profileImage)
                .firstName(firstName)
                .lastName(lastName)
                .dateOfBirth(dateOfBirth)
                .city(city)
                .bio(bio)
                .build();
    }
}
