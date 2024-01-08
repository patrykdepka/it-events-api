package pl.patrykdepka.iteventsapi.profileimage.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.profileimage.exception.DefaultProfileImageNotFoundException;
import pl.patrykdepka.iteventsapi.profileimage.model.ProfileImage;
import pl.patrykdepka.iteventsapi.profileimage.repository.ProfileImageRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class ProfileImageServiceImpl implements ProfileImageService {
    private final ProfileImageRepository profileImageRepository;

    public ProfileImageServiceImpl(ProfileImageRepository profileImageRepository) {
        this.profileImageRepository = profileImageRepository;
    }

    public ProfileImage createDefaultProfileImage() {
        ClassPathResource resource = new ClassPathResource("static/images/default_profile_image.png");
        try (InputStream defaultProfileImage = resource.getInputStream()) {
            ProfileImage profileImage = new ProfileImage();
            profileImage.setFileName(resource.getFilename());
            profileImage.setFileType("image/png");
            profileImage.setFileData(defaultProfileImage.readAllBytes());
            return profileImageRepository.save(profileImage);
        } catch (IOException e) {
            throw new DefaultProfileImageNotFoundException("File " + resource.getPath() + " not found");
        }
    }

    public Optional<ProfileImage> updateProfileImage(AppUser user, MultipartFile newProfileImage) {
        ProfileImage currentProfileImage = user.getProfileImage();
        try (InputStream is = newProfileImage.getInputStream()) {
            if (currentProfileImage.getFileData() != is.readAllBytes()) {
                currentProfileImage.setFileName(newProfileImage.getOriginalFilename());
                currentProfileImage.setFileType(newProfileImage.getContentType());
                currentProfileImage.setFileData(newProfileImage.getBytes());
                return Optional.of(currentProfileImage);
            }
            return Optional.empty();
        } catch (IOException e) {
            throw new DefaultProfileImageNotFoundException("File not found");
        }
    }
}
