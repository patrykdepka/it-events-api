package pl.patrykdepka.iteventsapi.event.domain;

import lombok.Getter;

@Getter
public enum EventType {
    MEETING("Spotkanie"),
    CONFERENCE("Konferencja");

    public final String displayName;

    EventType(String displayName) {
        this.displayName = displayName;
    }
}
