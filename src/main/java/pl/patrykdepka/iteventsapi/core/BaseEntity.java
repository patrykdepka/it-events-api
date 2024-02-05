package pl.patrykdepka.iteventsapi.core;

import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@EqualsAndHashCode(of = "uuid")
@Setter
public abstract class BaseEntity {
    private String uuid = UUID.randomUUID().toString();
}
