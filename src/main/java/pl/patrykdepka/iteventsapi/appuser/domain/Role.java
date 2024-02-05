package pl.patrykdepka.iteventsapi.appuser.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    ROLE_ADMIN("ADMIN", "Administrator"),
    ROLE_ORGANIZER("ORGANIZER", "Organizator"),
    ROLE_USER("USER", "Użytkownik");

    private final String role;
    private final String displayName;
}
