package pl.patrykdepka.iteventsapi.creator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.patrykdepka.iteventsapi.image.domain.Image;

import java.io.IOException;
import java.io.InputStream;

import static pl.patrykdepka.iteventsapi.image.domain.ImageType.EVENT_IMAGE;

public class EventImageCreator {

    public static Image createDefaultEventImage() {
        try {
            InputStream imagePath = new ClassPathResource("static/images/default_event_image.png").getInputStream();
            MockMultipartFile defaultEventImage = new MockMultipartFile("default_event_image.png", StreamUtils.copyToByteArray(imagePath));
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

    public static Image createDefaultEventImage(Long id) {
        try {
            InputStream imagePath = new ClassPathResource("static/images/default_event_image.png").getInputStream();
            MockMultipartFile defaultEventImage = new MockMultipartFile("default_event_image.png", StreamUtils.copyToByteArray(imagePath));
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

    public static MultipartFile createNewEventImageFile() {
        try {
            InputStream imagePath = new ClassPathResource("static/images/custom_event_image.png").getInputStream();
            return new MockMultipartFile(
                    "custom_event_image.png",
                    "custom_event_image.png",
                    "image/png",
                    StreamUtils.copyToByteArray(imagePath)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
