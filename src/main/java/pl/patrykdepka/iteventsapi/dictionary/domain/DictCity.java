package pl.patrykdepka.iteventsapi.dictionary.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.patrykdepka.iteventsapi.core.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class DictCity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String urnName;
    private String displayName;

    public DictCity(String urnName, String displayName) {
        this.urnName = urnName;
        this.displayName = displayName;
    }

    public static DictCityBuilder builder() {
        return new DictCityBuilder();
    }

    public static class DictCityBuilder {
        private Long id;
        private String uuid;
        private String urnName;
        private String displayName;

        public DictCityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DictCityBuilder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public DictCityBuilder urnName(String urnName) {
            this.urnName = urnName;
            return this;
        }

        public DictCityBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public DictCity build() {
            DictCity dictCity = new DictCity();
            dictCity.setId(id);
            dictCity.setUuid(uuid);
            dictCity.setUrnName(urnName);
            dictCity.setDisplayName(displayName);
            return dictCity;
        }
    }
}
