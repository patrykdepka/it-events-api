package pl.patrykdepka.iteventsapi.event.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.patrykdepka.iteventsapi.core.BaseIT;
import pl.patrykdepka.iteventsapi.creator.CreateEventDTOCreator;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventItemListDTO;
import pl.patrykdepka.iteventsapi.event.domain.dto.ParticipantDTO;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.patrykdepka.iteventsapi.creator.AppUserCreator.createEventParticipant;
import static pl.patrykdepka.iteventsapi.creator.AppUserCreator.createOrganizer;
import static pl.patrykdepka.iteventsapi.creator.EventEditDTOCreator.create;
import static pl.patrykdepka.iteventsapi.event.domain.AdmissionType.FREE;
import static pl.patrykdepka.iteventsapi.event.domain.EventType.MEETING;
import static pl.patrykdepka.iteventsapi.image.domain.ImageType.EVENT_IMAGE;
import static pl.patrykdepka.iteventsapi.image.domain.ImageType.PROFILE_IMAGE;

class OrganizerEventServiceIT extends BaseIT {

    @Autowired
    private OrganizerEventService organizerEventService;

    @Test
    void shouldCreateEvent() {
        // given
        var organizer = createOrganizer();
        var newEventData = CreateEventDTOCreator.create(NOW.plusMonths(1));
        // when
        var createdEvent = organizerEventService.createEvent(organizer, newEventData);
        // then
        assertThat(createdEvent).isNotNull();
        assertThat(createdEvent.getId()).isNotNull();
        assertThat(createdEvent.getName()).isEqualTo("Dev Meets #001");
        assertThat(createdEvent.getImageType()).isEqualTo(EVENT_IMAGE);
        assertThat(createdEvent.getImageData()).isNotBlank();
        assertThat(createdEvent.getEventType()).isEqualTo(MEETING.getDisplayName());
        assertThat(createdEvent.getDate()).isEqualTo(NOW.plusMonths(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertThat(createdEvent.getHour()).isEqualTo(NOW.plusMonths(1).format(DateTimeFormatter.ofPattern("HH:mm")));
        assertThat(createdEvent.getLanguage()).isEqualTo(newEventData.getLanguage());
        assertThat(createdEvent.getAdmission()).isEqualTo(FREE.getDisplayName());
        assertThat(createdEvent.getCity()).isEqualTo(newEventData.getCity());
        assertThat(createdEvent.getLocation()).isEqualTo(newEventData.getLocation());
        assertThat(createdEvent.getAddress()).isEqualTo(newEventData.getAddress());
        assertThat(createdEvent.getOrganizerId()).isEqualTo(organizer.getId());
        assertThat(createdEvent.getOrganizerImageType()).isEqualTo(PROFILE_IMAGE);
        assertThat(createdEvent.getOrganizerImageData()).isNotNull();
        assertThat(createdEvent.getOrganizerName()).isEqualTo("Organizer Organizer");
        assertThat(createdEvent.getDescription()).isEqualTo(newEventData.getDescription());
        assertThat(createdEvent.isCurrentUserIsParticipant()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void shouldReturnEventsWhereUserIsOrganizer(int pageNumber) {
        // given
        var organizer = createOrganizer();
        PageRequest page = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        // when
        Page<EventItemListDTO> events = organizerEventService.findOrganizerEvents(organizer, page);
        // then
        assertThat(events.getTotalPages()).isEqualTo(3);
        assertThat(events.getTotalElements()).isEqualTo(24);
        assertThat(events.getNumber()).isEqualTo(pageNumber);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void shouldReturnEventsByCityWhereUserIsOrganizer(int pageNumber) {
        // given
        var organizer = createOrganizer();
        var city = "Rzeszów";
        PageRequest page = PageRequest.of(pageNumber, 10, Sort.by(Sort.Direction.ASC, "dateTime"));
        // when
        Page<EventItemListDTO> events = organizerEventService.findOrganizerEventsByCity(organizer, city, page);
        // then
        assertThat(events.getTotalPages()).isEqualTo(2);
        assertThat(events.getTotalElements()).isEqualTo(12);
        assertThat(events.getNumber()).isEqualTo(pageNumber);
        events.getContent().forEach(e -> assertThat(e.getCity().equals(city)).isTrue());
    }

    @Test
    void shouldReturnEventToEdit() {
        // given
        var organizer = createOrganizer();
        // when
        var eventToEdit = organizerEventService.findEventToEdit(organizer, 6L);
        // then
        assertThat(eventToEdit.getName()).isEqualTo("Java Dev Talks #6");
        assertThat(eventToEdit.getEventImageData()).isNotBlank();
        assertThat(eventToEdit.getEventImage()).isNull();
        assertThat(eventToEdit.getEventType()).isEqualTo(MEETING);
        assertThat(eventToEdit.getDateTime()).isEqualTo(NOW.plusWeeks(2).format(ISO_LOCAL_DATE_TIME));
        assertThat(eventToEdit.getLanguage()).isEqualTo("polski");
        assertThat(eventToEdit.getAdmission()).isEqualTo(FREE);
        assertThat(eventToEdit.getCity()).isEqualTo("Rzeszów");
        assertThat(eventToEdit.getLocation()).isEqualTo("WSIiZ");
        assertThat(eventToEdit.getAddress()).isEqualTo("Sucharskiego 2, 35-225 Rzeszów");
        assertThat(eventToEdit.getDescription()).isEqualTo("Spotkanie rzeszowskiej grupy pasjonatów języka Java.");
    }

    @Test
    void shouldUpdateAndReturnUpdatedEventWithGivenId() {
        // given
        var organizer = createOrganizer();
        var newEventData = create(NOW.plusWeeks(2));
        // when
        var updatedEvent = organizerEventService.updateEvent(organizer, 6L, newEventData);
        // then
        assertThat(updatedEvent.getName()).isEqualTo(newEventData.getName());
        assertThat(updatedEvent.getEventImageData()).isNotBlank();
        assertThat(updatedEvent.getEventImage()).isNull();
        assertThat(updatedEvent.getEventType()).isEqualTo(newEventData.getEventType());
        assertThat(updatedEvent.getDateTime()).isEqualTo(newEventData.getDateTime());
        assertThat(updatedEvent.getLanguage()).isEqualTo(newEventData.getLanguage());
        assertThat(updatedEvent.getAdmission()).isEqualTo(newEventData.getAdmission());
        assertThat(updatedEvent.getCity()).isEqualTo(newEventData.getCity());
        assertThat(updatedEvent.getLocation()).isEqualTo(newEventData.getLocation());
        assertThat(updatedEvent.getAddress()).isEqualTo(newEventData.getAddress());
        assertThat(updatedEvent.getDescription()).isEqualTo(newEventData.getDescription());
    }

    @Test
    void shouldReturnEventParticipants() {
        // given
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "lastName"));
        // when
        Page<ParticipantDTO> participants = organizerEventService.findEventParticipants(createOrganizer(), 5L, page);
        // then
        assertThat(participants).isNotNull();
        assertThat(participants.getTotalPages()).isEqualTo(1);
        assertThat(participants.getTotalElements()).isEqualTo(6);
        assertThat(participants.getNumber()).isEqualTo(0);
    }

    @Test
    void shouldRemoveUserFromEventParticipantsAndReturnParticipantList() {
        // given
        var eventParticipant = createEventParticipant();
        PageRequest page = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "lastName"));
        // when
        Page<ParticipantDTO> participants = organizerEventService.removeParticipant(createOrganizer(), 5L, eventParticipant.getId(), page);
        // then
        assertThat(participants.getTotalPages()).isEqualTo(1);
        assertThat(participants.getTotalElements()).isEqualTo(5);
        assertThat(participants.getNumber()).isEqualTo(0);
        participants.getContent().forEach(p -> assertThat(p.getId().equals(eventParticipant.getId())).isFalse());
    }
}
