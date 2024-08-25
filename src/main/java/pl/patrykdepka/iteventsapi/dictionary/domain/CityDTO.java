package pl.patrykdepka.iteventsapi.dictionary.domain;

import lombok.Value;

import java.util.Objects;

@Value
public class CityDTO {
    String urnName;
    String displayName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityDTO cityDTO = (CityDTO) o;
        return Objects.equals(urnName, cityDTO.urnName) && Objects.equals(displayName, cityDTO.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(urnName, displayName);
    }
}
