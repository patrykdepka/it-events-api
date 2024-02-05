package pl.patrykdepka.iteventsapi.creator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StreamUtils;
import pl.patrykdepka.iteventsapi.image.domain.Image;
import pl.patrykdepka.iteventsapi.image.domain.dto.ImageDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import static pl.patrykdepka.iteventsapi.image.domain.ImageType.EVENT_IMAGE;
import static pl.patrykdepka.iteventsapi.image.domain.ImageType.PROFILE_IMAGE;

public class ImageCreator {

    public static Image createDefaultProfileImage() {
        try {
            InputStream imagePath = new ClassPathResource("static/images/default_profile_image.png").getInputStream();
            MockMultipartFile defaultProfileImage = new MockMultipartFile(
                    "default_profile_image.png",
                    "default_profile_image.png",
                    "image/png",
                    StreamUtils.copyToByteArray(imagePath)
            );
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

    public static Image createDefaultEventImage() {
        try {
            InputStream is = new ClassPathResource("static/images/default_event_image.png").getInputStream();
            MockMultipartFile defaultEventImage = new MockMultipartFile(
                    "default_event_image.png",
                    "default_event_image.png",
                    "image/png",
                    StreamUtils.copyToByteArray(is)
            );
            Image image = new Image();
            image.setType(EVENT_IMAGE);
            image.setFileName(defaultEventImage.getOriginalFilename());
            image.setContentType(defaultEventImage.getContentType());
            image.setFileData(defaultEventImage.getBytes());
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ImageDTO createCustomProfileImageData() {
        try {
            InputStream is = new ClassPathResource("static/images/custom_profile_image.png").getInputStream();
            MockMultipartFile customProfileImageData = new MockMultipartFile(
                    "custom_profile_image.png",
                    "custom_profile_image.png",
                    "image/png",
                    StreamUtils.copyToByteArray(is)
            );

            return new ImageDTO(
                    customProfileImageData.getOriginalFilename(),
                    Base64.getEncoder().encodeToString(customProfileImageData.getBytes()),
                    customProfileImageData.getContentType()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
