package pl.patrykdepka.iteventsapi.eventimage.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;
import pl.patrykdepka.iteventsapi.appuser.model.AppUser;
import pl.patrykdepka.iteventsapi.creator.AppUserCreator;
import pl.patrykdepka.iteventsapi.creator.EventCreator;
import pl.patrykdepka.iteventsapi.creator.EventImageCreator;
import pl.patrykdepka.iteventsapi.event.model.Event;
import pl.patrykdepka.iteventsapi.eventimage.model.EventImage;
import pl.patrykdepka.iteventsapi.eventimage.repository.EventImageRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static pl.patrykdepka.iteventsapi.appuser.model.Role.ROLE_ORGANIZER;

class EventImageServiceImplUnitTest {
    static final LocalDateTime DATE_TIME = LocalDateTime.now().withHour(18).withMinute(0);
    private EventImageRepository eventImageRepository;
    private EventImageServiceImpl eventImageServiceImpl;

    @BeforeEach
    void setUp() {
        eventImageRepository = Mockito.mock(EventImageRepository.class);
        eventImageServiceImpl = new EventImageServiceImpl(eventImageRepository);
    }

    @Test
    void shouldReturnSavedDefaultEventImage() {
        // given
        when(eventImageRepository.save(any(EventImage.class))).thenAnswer(i -> i.getArguments()[0]);
        // when
        EventImage createdDefaultEventImage = eventImageServiceImpl.createDefaultEventImage();
        // then
        verify(eventImageRepository, times(1)).save(argThat((EventImage savedDefaultEventImage) -> {
            Assertions.assertAll("Testing saved default event image",
                    () -> assertNull(savedDefaultEventImage.getId()),
                    () -> assertEquals(createdDefaultEventImage.getFileName(), savedDefaultEventImage.getFileName()),
                    () -> assertEquals(createdDefaultEventImage.getFileType(), savedDefaultEventImage.getFileType()),
                    () -> assertEquals(createdDefaultEventImage.getFileData(), savedDefaultEventImage.getFileData())
            );
            return true;
        }));
    }

    @Test
    void shouldReturnUpdatedEventImage() throws IOException {
        // given
        AppUser organizer = AppUserCreator.create(4L, "Jan", "Nowak", ROLE_ORGANIZER);
        Event event = EventCreator.create(1L, "Java Dev Talks #1", DATE_TIME, organizer);
        MultipartFile newEventImageFile = EventImageCreator.createNewEventImageFile();
        // when
        Optional<EventImage> updatedEventImage = eventImageServiceImpl.updateEventImage(event, newEventImageFile);
        // then
        assertThat(updatedEventImage.isPresent()).isTrue();
        assertThat(updatedEventImage.get().getFileName()).isEqualTo(newEventImageFile.getOriginalFilename());
        assertThat(updatedEventImage.get().getFileType()).isEqualTo(newEventImageFile.getContentType());
        assertThat(updatedEventImage.get().getFileData()).isEqualTo(newEventImageFile.getBytes());
    }
}
