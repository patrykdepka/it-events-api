package pl.patrykdepka.iteventsapi.image.domain;

import lombok.*;
import pl.patrykdepka.iteventsapi.core.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ImageType type;
    private String fileName;
    private String contentType;
    @Column(columnDefinition = "BLOB")
    private byte[] fileData;
}
