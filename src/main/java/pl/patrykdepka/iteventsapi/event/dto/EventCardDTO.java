package pl.patrykdepka.iteventsapi.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventCardDTO {
    private Long id;
    private String date;
    private String dayOfWeek;
    private String name;
    private String city;
    private String eventType;
    private String admission;

    public static EventCardDTOBuilder builder() {
        return new EventCardDTOBuilder();
    }

    public static class EventCardDTOBuilder {
        private Long id;
        private String date;
        private String dayOfWeek;
        private String name;
        private String city;
        private String eventType;
        private String admission;

        public EventCardDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EventCardDTOBuilder date(String date) {
            this.date = date;
            return this;
        }

        public EventCardDTOBuilder dayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
            return this;
        }

        public EventCardDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventCardDTOBuilder city(String city) {
            this.city = city;
            return this;
        }

        public EventCardDTOBuilder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public EventCardDTOBuilder admission(String admission) {
            this.admission = admission;
            return this;
        }

        public EventCardDTO build() {
            EventCardDTO eventCard = new EventCardDTO();
            eventCard.setId(id);
            eventCard.setDate(date);
            eventCard.setDayOfWeek(dayOfWeek);
            eventCard.setName(name);
            eventCard.setCity(city);
            eventCard.setEventType(eventType);
            eventCard.setAdmission(admission);
            return eventCard;
        }
    }
}
