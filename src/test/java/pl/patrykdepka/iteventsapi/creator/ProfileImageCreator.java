package pl.patrykdepka.iteventsapi.creator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.patrykdepka.iteventsapi.profileimage.model.ProfileImage;

import java.io.IOException;
import java.io.InputStream;

public class ProfileImageCreator {

    public static ProfileImage createDefaultProfileImage(Long id) {
        try {
            InputStream imagePath = new ClassPathResource("static/images/default_profile_image.png").getInputStream();
            MockMultipartFile defaultProfileImage = new MockMultipartFile("default_profile_image.png", StreamUtils.copyToByteArray(imagePath));
            return ProfileImage.builder()
                    .id(id)
                    .fileName(defaultProfileImage.getOriginalFilename())
                    .fileType(defaultProfileImage.getContentType())
                    .fileData(defaultProfileImage.getBytes())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MultipartFile createNewProfileImageFile() {
        try {
            InputStream imagePath = new ClassPathResource("static/images/custom_profile_image.png").getInputStream();
            return new MockMultipartFile("custom_profile_image.png", StreamUtils.copyToByteArray(imagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ProfileImage createDefaultProfileImage() {
        try {
            InputStream imagePath = new ClassPathResource("static/images/default_profile_image.png").getInputStream();
            MockMultipartFile defaultProfileImage = new MockMultipartFile("default_profile_image.png", StreamUtils.copyToByteArray(imagePath));
            return ProfileImage.builder()
                    .fileName(defaultProfileImage.getOriginalFilename())
                    .fileType(defaultProfileImage.getContentType())
                    .fileData(defaultProfileImage.getBytes())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
