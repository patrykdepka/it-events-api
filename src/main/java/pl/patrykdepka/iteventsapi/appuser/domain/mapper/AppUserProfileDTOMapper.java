package pl.patrykdepka.iteventsapi.appuser.domain.mapper;

import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileDTO;
import pl.patrykdepka.iteventsapi.image.domain.Image;

import java.util.Base64;

public class AppUserProfileDTOMapper {

    private AppUserProfileDTOMapper() {
    }

    public static AppUserProfileDTO mapToAppUserProfileDTO(AppUser user) {
        return AppUserProfileDTO.builder()
                .id(user.getId())
                .contentType(user.getProfileImage().getContentType())
                .profileImageData(convertImageToBase64String(user.getProfileImage()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth().toString())
                .city(user.getCity())
                .bio(user.getBio())
                .build();
    }

    private static String convertImageToBase64String(Image image) {
        return "data:" + image.getContentType() + ";base64," + Base64.getEncoder().encodeToString(image.getFileData());
    }
}
