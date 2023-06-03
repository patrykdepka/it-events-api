package pl.patrykdepka.iteventsapi.profileimage.model;

import lombok.Getter;
import lombok.Setter;
import pl.patrykdepka.iteventsapi.core.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ProfileImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    @Column(columnDefinition = "BLOB")
    private byte[] fileData;

    public static ProfileImageBuilder builder() {
        return new ProfileImageBuilder();
    }

    public static class ProfileImageBuilder {
        private Long id;
        private String fileName;
        private String fileType;
        private byte[] fileData;

        public ProfileImageBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProfileImageBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public ProfileImageBuilder fileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public ProfileImageBuilder fileData(byte[] fileData) {
            this.fileData = fileData;
            return this;
        }

        public ProfileImage build() {
            ProfileImage profileImage = new ProfileImage();
            profileImage.setId(id);
            profileImage.setFileName(fileName);
            profileImage.setFileType(fileType);
            profileImage.setFileData(fileData);
            return profileImage;
        }
    }
}
