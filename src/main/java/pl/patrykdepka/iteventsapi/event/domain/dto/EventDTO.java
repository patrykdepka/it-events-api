package pl.patrykdepka.iteventsapi.event.domain.dto;

import lombok.Value;
import pl.patrykdepka.iteventsapi.image.domain.ImageType;

@Value
public class EventDTO {
    Long id;
    String name;
    ImageType imageType;
    String imageData;
    String eventType;
    String date;
    String hour;
    String language;
    String admission;
    String city;
    String location;
    String address;
    Long organizerId;
    ImageType organizerImageType;
    String organizerImageData;
    String organizerName;
    String description;
    boolean currentUserIsParticipant;

    public static class EventDTOBuilder {
        private Long id;
        private String name;
        private ImageType imageType;
        private String imageData;
        private String eventType;
        private String date;
        private String hour;
        private String language;
        private String admission;
        private String city;
        private String location;
        private String address;
        private Long organizerId;
        private ImageType organizerImageType;
        private String organizerImageData;
        private String organizerName;
        private String description;
        private boolean currentUserIsParticipant;

        public EventDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EventDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventDTOBuilder imageType(ImageType imageType) {
            this.imageType = imageType;
            return this;
        }

        public EventDTOBuilder imageData(String imageData) {
            this.imageData = imageData;
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

        public EventDTOBuilder organizerImageType(ImageType organizerImageType) {
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

        public EventDTOBuilder currentUserIsParticipant(boolean currentUserIsParticipant) {
            this.currentUserIsParticipant = currentUserIsParticipant;
            return this;
        }

        public EventDTO build() {
            return new EventDTO(
                    id,
                    name,
                    imageType,
                    imageData,
                    eventType,
                    date,
                    hour,
                    language,
                    admission,
                    city,
                    location,
                    address,
                    organizerId,
                    organizerImageType,
                    organizerImageData,
                    organizerName,
                    description,
                    currentUserIsParticipant
            );
        }
    }
}
