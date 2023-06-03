package pl.patrykdepka.iteventsapi.appuser.mapper;

import pl.patrykdepka.iteventsapi.appuser.dto.AdminAppUserProfileEditDTO;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;

import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class AdminAppUserProfileEditDTOMapper {

    private AdminAppUserProfileEditDTOMapper() {
    }

    public static AdminAppUserProfileEditDTO mapToAdminAppUserProfileEditDTO(AppUser user) {
        return AdminAppUserProfileEditDTO.builder()
                .id(user.getId())
                .profileImageType(user.getProfileImage().getFileType())
                .profileImageData(Base64.getEncoder().encodeToString(user.getProfileImage().getFileData()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .city(user.getCity())
                .bio(user.getBio())
                .build();
    }
}
