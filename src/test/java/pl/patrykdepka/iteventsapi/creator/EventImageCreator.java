package pl.patrykdepka.iteventsapi.creator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.patrykdepka.iteventsapi.eventimage.model.EventImage;

import java.io.IOException;
import java.io.InputStream;

public class EventImageCreator {

    public static EventImage createDefaultEventImage() {
        try {
            InputStream imagePath = new ClassPathResource("static/images/default_event_image.png").getInputStream();
            MockMultipartFile defaultEventImage = new MockMultipartFile("default_event_image.png", StreamUtils.copyToByteArray(imagePath));
            return EventImage.builder()
                    .fileName(defaultEventImage.getOriginalFilename())
                    .fileType(defaultEventImage.getContentType())
                    .fileData(defaultEventImage.getBytes())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static EventImage createDefaultEventImage(Long id) {
        try {
            InputStream imagePath = new ClassPathResource("static/images/default_event_image.png").getInputStream();
            MockMultipartFile defaultEventImage = new MockMultipartFile("default_event_image.png", StreamUtils.copyToByteArray(imagePath));
            return EventImage.builder()
                    .id(id)
                    .fileName(defaultEventImage.getOriginalFilename())
                    .fileType(defaultEventImage.getContentType())
                    .fileData(defaultEventImage.getBytes())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MultipartFile createNewEventImageFile() {
        try {
            InputStream imagePath = new ClassPathResource("static/images/custom_event_image.png").getInputStream();
            return new MockMultipartFile("custom_event_image.png", StreamUtils.copyToByteArray(imagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
