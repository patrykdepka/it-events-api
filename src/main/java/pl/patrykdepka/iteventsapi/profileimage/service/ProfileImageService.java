package pl.patrykdepka.iteventsapi.profileimage.service;

import org.springframework.web.multipart.MultipartFile;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;
import pl.patrykdepka.iteventsapi.profileimage.model.ProfileImage;

import java.util.Optional;

public interface ProfileImageService {

    ProfileImage createDefaultProfileImage();

    Optional<ProfileImage> updateProfileImage(AppUser user, MultipartFile newProfileImage);
}
