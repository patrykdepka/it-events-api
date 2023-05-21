package pl.patrykdepka.iteventsapp.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDTO {
    private Long id;
    private String name;
    private String eventType;
    private String date;
    private String hour;
    private String language;
    private String admission;
    private String city;
    private String location;
    private String address;
    private Long organizerId;
    private String organizerImageType;
    private String organizerImageData;
    private String organizerName;
    private String description;

    public static class EventDTOBuilder {
        private Long id;
        private String name;
        private String eventType;
        private String date;
        private String hour;
        private String language;
        private String admission;
        private String city;
        private String location;
        private String address;
        private Long organizerId;
        private String organizerImageType;
        private String organizerImageData;
        private String organizerName;
        private String description;

        public EventDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EventDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventDTOBuilder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public EventDTOBuilder date(String date) {
            this.date = date;
            return this;
        }

        public EventDTOBuilder hour(String hour) {
            this.hour = hour;
            return this;
        }

        public EventDTOBuilder language(String language) {
            this.language = language;
            return this;
        }

        public EventDTOBuilder admission(String admission) {
            this.admission = admission;
            return this;
        }

        public EventDTOBuilder city(String city) {
            this.city = city;
            return this;
        }

        public EventDTOBuilder location(String location) {
            this.location = location;
            return this;
        }

        public EventDTOBuilder address(String address) {
            this.address = address;
            return this;
        }

        public EventDTOBuilder organizerId(Long organizerId) {
            this.organizerId = organizerId;
            return this;
        }

        public EventDTOBuilder organizerImageType(String organizerImageType) {
            this.organizerImageType = organizerImageType;
            return this;
        }

        public EventDTOBuilder organizerImageData(String organizerImageData) {
            this.organizerImageData = organizerImageData;
            return this;
        }

        public EventDTOBuilder organizerName(String organizerName) {
            this.organizerName = organizerName;
            return this;
        }

        public EventDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventDTO build() {
            EventDTO event = new EventDTO();
            event.setId(id);
            event.setName(name);
            event.setEventType(eventType);
            event.setDate(date);
            event.setHour(hour);
            event.setLanguage(language);
            event.setAdmission(admission);
            event.setCity(city);
            event.setLocation(location);
            event.setAddress(address);
            event.setOrganizerId(organizerId);
            event.setOrganizerImageType(organizerImageType);
            event.setOrganizerImageData(organizerImageData);
            event.setOrganizerName(organizerName);
            event.setDescription(description);
            return event;
        }
    }
}
