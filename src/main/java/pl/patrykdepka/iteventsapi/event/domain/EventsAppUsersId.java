package pl.patrykdepka.iteventsapi.event.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class EventsAppUsersId implements Serializable {
    @Column(name = "event_id")
    private Long event;
    @Column(name = "app_user_id")
    private Long appUserId;
}
