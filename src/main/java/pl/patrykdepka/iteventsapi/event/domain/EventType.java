package pl.patrykdepka.iteventsapi.event.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EventType {
    MEETING("Spotkanie"),
    CONFERENCE("Konferencja"),
    ONLINE("Online");

    public final String displayName;
}
