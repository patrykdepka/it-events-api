package pl.patrykdepka.iteventsapi.event.mapper;

import org.springframework.data.domain.Page;
import pl.patrykdepka.iteventsapi.event.dto.EventCardDTO;
import pl.patrykdepka.iteventsapi.event.model.Event;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EventCardDTOMapper {

    public static List<EventCardDTO> mapToEventCardDTOs(List<Event> events) {
        return events
                .stream()
                .map(EventCardDTOMapper::mapToEventCardDTO)
                .collect(Collectors.toList());
    }

    public static Page<EventCardDTO> mapToEventCardDTOs(Page<Event> events) {
        return events.map(EventCardDTOMapper::mapToEventCardDTO);
    }

    private static EventCardDTO mapToEventCardDTO(Event event) {
        return new EventCardDTO.EventCardDTOBuilder()
                .id(event.getId())
                .date(event.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM")))
                .dayOfWeek(event.getDateTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()))
                .name(event.getName())
                .city(event.getCity())
                .eventType(event.getEventType().getDisplayName())
                .admission(event.getAdmission().getDisplayName())
                .build();
    }
}
