package pl.patrykdepka.iteventsapi.appuser.domain.mapper;

import pl.patrykdepka.iteventsapi.appuser.domain.dto.AppUserProfileDTO;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;

import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class AppUserProfileDTOMapper {

    private AppUserProfileDTOMapper() {
    }

    public static AppUserProfileDTO mapToAppUserProfileDTO(AppUser user) {
        return AppUserProfileDTO.builder()
                .id(user.getId())
                .profileImageType(user.getProfileImage().getType())
                .profileImageData(Base64.getEncoder().encodeToString(user.getProfileImage().getFileData()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .city(user.getCity())
                .bio(user.getBio())
                .build();
    }
}
