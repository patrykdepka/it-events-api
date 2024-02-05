package pl.patrykdepka.iteventsapi.event.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "events_app_users")
@NoArgsConstructor
@Getter
public class EventsAppUsers {
    @EmbeddedId
    private EventsAppUsersId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Event event;
    @Column(name = "app_user_id", nullable = false, insertable = false, updatable = false)
    private Long appUserId;

    public EventsAppUsers(Event event, Long appUserId) {
        this.id = new EventsAppUsersId(event.getId(), appUserId);
        this.event = event;
        this.appUserId = appUserId;
    }
}
