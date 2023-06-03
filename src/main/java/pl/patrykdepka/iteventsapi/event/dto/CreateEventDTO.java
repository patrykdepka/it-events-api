package pl.patrykdepka.iteventsapi.event.dto;

import lombok.*;
import pl.patrykdepka.iteventsapi.event.enumeration.AdmissionType;
import pl.patrykdepka.iteventsapi.event.enumeration.EventType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDTO {
    @NotNull(message = "{form.field.name.error.notNull.message}")
    @NotEmpty(message = "{form.field.name.error.notEmpty.message}")
    private String name;
    @NotNull(message = "{form.field.eventType.error.notNull.message}")
    private EventType eventType;
    @NotNull(message = "{form.field.dateTime.error.notNull.message}")
    @NotEmpty(message = "{form.field.dateTime.error.notEmpty.message}")
    private String dateTime;
    @NotNull(message = "{form.field.language.error.notNull.message}")
    @NotEmpty(message = "{form.field.language.error.notEmpty.message}")
    private String language;
    @NotNull(message = "{form.field.admission.error.notNull.message}")
    private AdmissionType admission;
    @NotNull(message = "{form.field.city.error.notNull.message}")
    @NotEmpty(message = "{form.field.city.error.notEmpty.message}")
    private String city;
    @NotNull(message = "{form.field.location.error.notNull.message}")
    @NotEmpty(message = "{form.field.location.error.notEmpty.message}")
    private String location;
    @NotNull(message = "{form.field.address.error.notNull.message}")
    @NotEmpty(message = "{form.field.address.error.notEmpty.message}")
    private String address;
    @NotNull(message = "{form.field.description.error.notNull.message}")
    @NotEmpty(message = "{form.field.description.error.notEmpty.message}")
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
