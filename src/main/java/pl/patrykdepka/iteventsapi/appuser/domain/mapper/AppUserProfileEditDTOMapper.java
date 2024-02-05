package pl.patrykdepka.iteventsapi.appuser.domain.mapper;

import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileEditDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.image.domain.Image;

import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class AppUserProfileEditDTOMapper {

    private AppUserProfileEditDTOMapper() {
    }

    public static AppUserProfileEditDTO mapToAppUserProfileEditDTO(AppUser user) {
        return AppUserProfileEditDTO.builder()
                .currentProfileImageData(convertImageToBase64String(user.getProfileImage()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .city(user.getCity())
                .bio(user.getBio())
                .build();
    }

    private static String convertImageToBase64String(Image image) {
        return "data:" + image.getContentType() + ";base64," + Base64.getEncoder().encodeToString(image.getFileData());
    }
}
