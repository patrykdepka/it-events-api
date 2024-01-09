package pl.patrykdepka.iteventsapi.eventimage.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.patrykdepka.iteventsapi.event.domain.Event;
import pl.patrykdepka.iteventsapi.eventimage.exception.DefaultEventImageNotFoundException;
import pl.patrykdepka.iteventsapi.eventimage.model.EventImage;
import pl.patrykdepka.iteventsapi.eventimage.repository.EventImageRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class EventImageServiceImpl implements EventImageService {
    private final EventImageRepository eventImageRepository;

    public EventImageServiceImpl(EventImageRepository eventImageRepository) {
        this.eventImageRepository = eventImageRepository;
    }

    public EventImage createDefaultEventImage() {
        ClassPathResource resource = new ClassPathResource("static/images/default_event_image.png");
        try (InputStream defaultEventImage = resource.getInputStream()) {
            EventImage eventImage = new EventImage();
            eventImage.setFileName(resource.getFilename());
            eventImage.setFileType("image/png");
            eventImage.setFileData(defaultEventImage.readAllBytes());
            return eventImageRepository.save(eventImage);
        } catch (IOException e) {
            throw new DefaultEventImageNotFoundException("File " + resource.getPath() + " not found");
        }
    }

    public Optional<EventImage> updateEventImage(Event event, MultipartFile newEventImage) {
        EventImage currentEventImage = event.getEventImage();
        try (InputStream is = newEventImage.getInputStream()) {
            if (currentEventImage.getFileData() != is.readAllBytes()) {
                currentEventImage.setFileName(newEventImage.getOriginalFilename());
                currentEventImage.setFileType(newEventImage.getContentType());
                currentEventImage.setFileData(newEventImage.getBytes());
                return Optional.of(currentEventImage);
            }
            return Optional.empty();
        } catch (IOException e) {
            throw new DefaultEventImageNotFoundException("File not found");
        }
    }
}
