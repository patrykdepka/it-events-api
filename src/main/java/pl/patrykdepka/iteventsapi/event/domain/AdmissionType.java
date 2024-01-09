package pl.patrykdepka.iteventsapi.event.domain;

import lombok.Getter;

@Getter
public enum AdmissionType {
    PAID("Płatny"),
    FREE("Bezpłatny");

    public final String displayName;

    AdmissionType(String displayName) {
        this.displayName = displayName;
    }
}
