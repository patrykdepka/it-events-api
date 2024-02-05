package pl.patrykdepka.iteventsapi.event.domain.mapper;

import org.springframework.data.domain.Page;
import pl.patrykdepka.iteventsapi.event.domain.Event;
import pl.patrykdepka.iteventsapi.event.domain.dto.EventItemListDTO;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EventItemListDTOMapper {

    public static List<EventItemListDTO> mapToEventItemListDTOs(List<Event> events) {
        return events.stream()
                .map(EventItemListDTOMapper::mapToEventItemListDTO)
                .collect(Collectors.toList());
    }

    public static Page<EventItemListDTO> mapToEventItemListDTOs(Page<Event> events) {
        return events.map(EventItemListDTOMapper::mapToEventItemListDTO);
    }

    public static EventItemListDTO mapToEventItemListDTO(Event event) {
        return new EventItemListDTO.EventCardDTOBuilder()
                .id(event.getId())
                .date(event.getDateTime().toLocalDate())
                .dayOfWeek(event.getDateTime().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()))
                .name(event.getName())
                .city(event.getCity())
                .eventType(event.getEventType().getDisplayName())
                .admission(event.getAdmission().getDisplayName())
                .build();
    }
}
