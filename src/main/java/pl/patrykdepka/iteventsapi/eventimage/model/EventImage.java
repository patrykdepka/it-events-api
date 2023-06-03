package pl.patrykdepka.iteventsapi.eventimage.model;

import lombok.Getter;
import lombok.Setter;
import pl.patrykdepka.iteventsapi.core.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class EventImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    @Column(columnDefinition = "BLOB")
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
