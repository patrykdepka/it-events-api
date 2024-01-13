package pl.patrykdepka.iteventsapi.event.domain;

import lombok.Getter;
import lombok.Setter;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.core.BaseEntity;
import pl.patrykdepka.iteventsapi.image.domain.Image;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_event_image",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_image_id", referencedColumnName = "id")
    )
    private Image eventImage;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private LocalDateTime dateTime;
    private String language;
    @Enumerated(EnumType.STRING)
    private AdmissionType admission;
    private String city;
    private String location;
    private String address;
    @OneToOne
    @JoinColumn(name = "organizer_id")
    private AppUser organizer;
    private String description;
    @OneToMany
    @JoinTable(
            name = "event_app_user",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    )
    private List<AppUser> participants = new ArrayList<>();

    public void addParticipant(AppUser user) {
        participants.add(user);
    }

    public void removeParticipant(AppUser user) {
        participants.remove(user);
    }

    public boolean checkIfUserIsParticipant(AppUser user) {
        return participants.contains(user);
    }

    public static EventBuilder builder() {
        return new EventBuilder();
    }

    public static class EventBuilder {
        private Long id;
        private String name;
        private Image eventImage;
        private EventType eventType;
        private LocalDateTime dateTime;
        private String language;
        private AdmissionType admission;
        private String city;
        private String location;
        private String address;
        private AppUser organizer;
        private String description;
        private List<AppUser> participants;

        public EventBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EventBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventBuilder eventImage(Image eventImage) {
            this.eventImage = eventImage;
            return this;
        }

        public EventBuilder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public EventBuilder dateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public EventBuilder language(String language) {
            this.language = language;
            return this;
        }

        public EventBuilder admission(AdmissionType admission) {
            this.admission = admission;
            return this;
        }

        public EventBuilder city(String city) {
            this.city = city;
            return this;
        }

        public EventBuilder location(String location) {
            this.location = location;
            return this;
        }

        public EventBuilder address(String address) {
            this.address = address;
            return this;
        }

        public EventBuilder organizer(AppUser organizer) {
            this.organizer = organizer;
            return this;
        }

        public EventBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventBuilder participants(List<AppUser> participants) {
            this.participants = participants;
            return this;
        }

        public Event build() {
            Event event = new Event();
            event.setId(id);
            event.setName(name);
            event.setEventImage(eventImage);
            event.setEventType(eventType);
            event.setDateTime(dateTime);
            event.setLanguage(language);
            event.setAdmission(admission);
            event.setCity(city);
            event.setLocation(location);
            event.setAddress(address);
            event.setOrganizer(organizer);
            event.setDescription(description);
            event.setParticipants(participants);
            return event;
        }
    }
}
