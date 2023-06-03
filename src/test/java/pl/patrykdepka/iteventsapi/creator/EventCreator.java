package pl.patrykdepka.iteventsapi.creator;

import pl.patrykdepka.iteventsapi.appuser.model.AppUser;
import pl.patrykdepka.iteventsapi.event.enumeration.AdmissionType;
import pl.patrykdepka.iteventsapi.event.enumeration.EventType;
import pl.patrykdepka.iteventsapi.event.model.Event;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class EventCreator {

    public static Event create(String name, LocalDateTime localDateTime, AppUser organizer) {
        return Event.builder()
                .name(name)
                .eventImage(EventImageCreator.createDefaultEventImage())
                .eventType(EventType.MEETING)
                .dateTime(localDateTime.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)))
                .language("polski")
                .admission(AdmissionType.FREE)
                .city("Rzeszów")
                .location("WSIiZ")
                .address("Sucharskiego 2, 35-225 Rzeszów")
                .organizer(organizer)
                .description("Spotkanie rzeszowskiej grupy pasjonatów języka Java.")
                .participants(new ArrayList<>())
                .build();
    }

    public static Event create(Long id, String name, LocalDateTime localDateTime, AppUser organizer) {
        return Event.builder()
                .id(id)
                .name(name)
                .eventImage(EventImageCreator.createDefaultEventImage(id))
                .eventType(EventType.MEETING)
                .dateTime(localDateTime.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)))
                .language("polski")
                .admission(AdmissionType.FREE)
                .city("Rzeszów")
                .location("WSIiZ")
                .address("Sucharskiego 2, 35-225 Rzeszów")
                .organizer(organizer)
                .description("Spotkanie rzeszowskiej grupy pasjonatów języka Java.")
                .participants(new ArrayList<>())
                .build();
    }

    public static Event create(Long id, String name, LocalDateTime localDateTime, AppUser organizer, List<AppUser> participants) {
        return Event.builder()
                .id(id)
                .name(name)
                .eventImage(EventImageCreator.createDefaultEventImage(id))
                .eventType(EventType.MEETING)
                .dateTime(localDateTime.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)))
                .language("polski")
                .admission(AdmissionType.FREE)
                .city("Rzeszów")
                .location("WSIiZ")
                .address("Sucharskiego 2, 35-225 Rzeszów")
                .organizer(organizer)
                .description("Spotkanie rzeszowskiej grupy pasjonatów języka Java.")
                .participants(participants)
                .build();
    }
}
