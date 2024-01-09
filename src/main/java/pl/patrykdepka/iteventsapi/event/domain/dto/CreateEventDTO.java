package pl.patrykdepka.iteventsapi.event.domain.dto;

import lombok.*;
import pl.patrykdepka.iteventsapi.core.DateTime;
import pl.patrykdepka.iteventsapi.event.domain.AdmissionType;
import pl.patrykdepka.iteventsapi.event.domain.EventType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDTO {
    @NotBlank(message = "{form.field.name.error.notBlank.message}")
    private String name;
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

    public static class CreateEventDTOBuilder {
        private String name;
        private EventType eventType;
        private String dateTime;
        private String language;
        private AdmissionType admission;
        private String city;
        private String location;
        private String address;
        private String description;

        public CreateEventDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CreateEventDTOBuilder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public CreateEventDTOBuilder dateTime(String dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public CreateEventDTOBuilder language(String language) {
            this.language = language;
            return this;
        }

        public CreateEventDTOBuilder admission(AdmissionType admission) {
            this.admission = admission;
            return this;
        }

        public CreateEventDTOBuilder city(String city) {
            this.city = city;
            return this;
        }

        public CreateEventDTOBuilder location(String location) {
            this.location = location;
            return this;
        }

        public CreateEventDTOBuilder address(String address) {
            this.address = address;
            return this;
        }

        public CreateEventDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CreateEventDTO build() {
            CreateEventDTO newEventData = new CreateEventDTO();
            newEventData.setName(name);
            newEventData.setEventType(eventType);
            newEventData.setDateTime(dateTime);
            newEventData.setLanguage(language);
            newEventData.setAdmission(admission);
            newEventData.setCity(city);
            newEventData.setLocation(location);
            newEventData.setAddress(address);
            newEventData.setDescription(description);
            return newEventData;
        }
    }
}
