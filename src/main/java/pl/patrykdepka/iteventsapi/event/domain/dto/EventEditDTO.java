package pl.patrykdepka.iteventsapi.event.domain.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import pl.patrykdepka.iteventsapi.core.DateTime;
import pl.patrykdepka.iteventsapi.core.Image;
import pl.patrykdepka.iteventsapi.event.domain.AdmissionType;
import pl.patrykdepka.iteventsapi.event.domain.EventType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEditDTO {
    @NotBlank(message = "{form.field.name.error.notBlank.message}")
    private String name;
    private String imageType;
    private String imageData;
    @Image(width = 480, height = 270)
    private MultipartFile eventImage;
    @NotNull(message = "{form.field.eventType.error.notNull.message}")
    private EventType eventType;
    @NotNull(message = "{form.field.dateOfBirth.error.notNull.message}")
    @DateTime(message = "{validation.annotation.DateTime.dateTime.invalidFormat.message}", iso = DateTime.ISO.DATE_TIME)
    private String dateTime;
    @NotBlank(message = "{form.field.language.error.notBlank.message}")
    private String language;
    @NotNull(message = "{form.field.admission.error.notNull.message}")
    private AdmissionType admission;
    @NotBlank(message = "{form.field.city.error.notBlank.message}")
    private String city;
    @NotBlank(message = "{form.field.location.error.notBlank.message}")
    private String location;
    @NotBlank(message = "{form.field.address.error.notBlank.message}")
    private String address;
    @NotBlank(message = "{form.field.description.error.notBlank.message}")
    private String description;

    public static EventEditDTOBuilder builder() {
        return new EventEditDTOBuilder();
    }

    public static class EventEditDTOBuilder {
        private String name;
        private String imageType;
        private String imageData;
        private MultipartFile eventImage;
        private EventType eventType;
        private String dateTime;
        private String language;
        private AdmissionType admission;
        private String city;
        private String location;
        private String address;
        private String description;

        public EventEditDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventEditDTOBuilder imageType(String imageType) {
            this.imageType = imageType;
            return this;
        }

        public EventEditDTOBuilder imageData(String imageData) {
            this.imageData = imageData;
            return this;
        }

        public EventEditDTOBuilder eventImage(MultipartFile eventImage) {
            this.eventImage = eventImage;
            return this;
        }

        public EventEditDTOBuilder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public EventEditDTOBuilder dateTime(String dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public EventEditDTOBuilder language(String language) {
            this.language = language;
            return this;
        }

        public EventEditDTOBuilder admission(AdmissionType admission) {
            this.admission = admission;
            return this;
        }

        public EventEditDTOBuilder city(String city) {
            this.city = city;
            return this;
        }

        public EventEditDTOBuilder location(String location) {
            this.location = location;
            return this;
        }

        public EventEditDTOBuilder address(String address) {
            this.address = address;
            return this;
        }

        public EventEditDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventEditDTO build() {
            EventEditDTO eventData = new EventEditDTO();
            eventData.setName(name);
            eventData.setImageType(imageType);
            eventData.setImageData(imageData);
            eventData.setEventImage(eventImage);
            eventData.setEventType(eventType);
            eventData.setDateTime(dateTime);
            eventData.setLanguage(language);
            eventData.setAdmission(admission);
            eventData.setCity(city);
            eventData.setLocation(location);
            eventData.setAddress(address);
            eventData.setDescription(description);
            return eventData;
        }
    }
}
