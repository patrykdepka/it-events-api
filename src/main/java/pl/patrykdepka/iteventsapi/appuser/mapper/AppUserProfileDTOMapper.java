package pl.patrykdepka.iteventsapi.appuser.mapper;

import pl.patrykdepka.iteventsapi.appuser.dto.AppUserProfileDTO;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;

import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class AppUserProfileDTOMapper {

    private AppUserProfileDTOMapper() {
    }

    public static AppUserProfileDTO mapToAppUserProfileDTO(AppUser user) {
        return AppUserProfileDTO.builder()
                .profileImageType(user.getProfileImage().getFileType())
                .profileImageData(Base64.getEncoder().encodeToString(user.getProfileImage().getFileData()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .city(user.getCity())
                .bio(user.getBio())
                .build();
    }
}
