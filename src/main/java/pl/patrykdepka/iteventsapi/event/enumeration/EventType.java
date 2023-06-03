package pl.patrykdepka.iteventsapi.event.enumeration;

public enum EventType {
    MEETING("Spotkanie"),
    CONFERENCE("Konferencja");

    public final String displayName;

    EventType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
