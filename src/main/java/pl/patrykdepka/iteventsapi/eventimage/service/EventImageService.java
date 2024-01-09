package pl.patrykdepka.iteventsapi.eventimage.service;

import org.springframework.web.multipart.MultipartFile;
import pl.patrykdepka.iteventsapi.event.domain.Event;
import pl.patrykdepka.iteventsapi.eventimage.model.EventImage;

import java.util.Optional;

public interface EventImageService {

    EventImage createDefaultEventImage();

    Optional<EventImage> updateEventImage(Event event, MultipartFile newEventImage);
}
