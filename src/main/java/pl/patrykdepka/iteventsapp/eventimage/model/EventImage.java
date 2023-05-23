package pl.patrykdepka.iteventsapp.eventimage.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class EventImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    private byte[] fileData;

    public static EventImageBuilder builder() {
        return new EventImageBuilder();
    }

    public static class EventImageBuilder {
        private Long id;
        private String fileName;
        private String fileType;
        private byte[] fileData;

        public EventImageBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EventImageBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public EventImageBuilder fileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public EventImageBuilder fileData(byte[] fileData) {
            this.fileData = fileData;
            return this;
        }

        public EventImage build() {
            EventImage eventImage = new EventImage();
            eventImage.setId(id);
            eventImage.setFileName(fileName);
            eventImage.setFileType(fileType);
            eventImage.setFileData(fileData);
            return eventImage;
        }
    }
}