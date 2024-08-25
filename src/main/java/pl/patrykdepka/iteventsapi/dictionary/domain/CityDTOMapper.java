package pl.patrykdepka.iteventsapi.dictionary.domain;

import java.util.List;
import java.util.stream.Collectors;

public class CityDTOMapper {

    public static List<CityDTO> mapToCityDTOs(List<DictCity> cities) {
        return cities.stream()
                .map(CityDTOMapper::mapToCityDto)
                .collect(Collectors.toList());
    }

    public static CityDTO mapToCityDto(DictCity city) {
        return new CityDTO(city.getUrnName(), city.getDisplayName());
    }
}
