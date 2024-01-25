package pl.patrykdepka.iteventsapi.creator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.patrykdepka.iteventsapi.image.domain.Image;
import pl.patrykdepka.iteventsapi.image.domain.dto.ImageDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import static pl.patrykdepka.iteventsapi.image.domain.ImageType.PROFILE_IMAGE;

public class ProfileImageCreator {

    public static Image createDefaultProfileImage(Long id) {
        try {
            InputStream imagePath = new ClassPathResource("static/images/default_profile_image.png").getInputStream();
            MockMultipartFile defaultProfileImage = new MockMultipartFile("default_profile_image.png", StreamUtils.copyToByteArray(imagePath));
            Image image = new Image();
            image.setType(PROFILE_IMAGE);
            image.setFileName(defaultProfileImage.getOriginalFilename());
            image.setContentType(defaultProfileImage.getContentType());
            image.setFileData(defaultProfileImage.getBytes());
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MultipartFile createNewProfileImageFile() {
        try {
            InputStream imagePath = new ClassPathResource("static/images/custom_profile_image.png").getInputStream();
            return new MockMultipartFile(
                    "custom_profile_image.png",
                    "custom_profile_image.png",
                    "image/png",
                    StreamUtils.copyToByteArray(imagePath)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Image createDefaultProfileImage() {
        try {
            InputStream imagePath = new ClassPathResource("static/images/default_profile_image.png").getInputStream();
            MockMultipartFile defaultProfileImage = new MockMultipartFile("default_profile_image.png", StreamUtils.copyToByteArray(imagePath));
            Image image = new Image();
            image.setType(PROFILE_IMAGE);
            image.setFileName(defaultProfileImage.getOriginalFilename());
            image.setContentType(defaultProfileImage.getContentType());
            image.setFileData(defaultProfileImage.getBytes());
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ImageDTO createNewProfileImageDTO() {
        try {
            ClassPathResource resource = new ClassPathResource("static/images/custom_profile_image.png");
            MockMultipartFile multipartFile = new MockMultipartFile(
                    resource.getFilename(), null, "image/png", resource.getInputStream()
            );
            return new ImageDTO(
                    multipartFile.getOriginalFilename(),
                    Base64.getEncoder().encodeToString(multipartFile.getBytes()),
                    multipartFile.getContentType()
            );
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
