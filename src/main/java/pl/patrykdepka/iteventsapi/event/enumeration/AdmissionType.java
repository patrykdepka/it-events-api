package pl.patrykdepka.iteventsapi.event.enumeration;

public enum AdmissionType {
    PAID("Płatny"),
    FREE("Bezpłatny");

    public final String displayName;

    AdmissionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
