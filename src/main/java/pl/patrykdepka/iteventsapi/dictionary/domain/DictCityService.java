package pl.patrykdepka.iteventsapi.dictionary.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.patrykdepka.iteventsapi.event.domain.exception.CityNotFoundException;

import java.util.List;
import java.util.Optional;

import static pl.patrykdepka.iteventsapi.dictionary.domain.CityDTOMapper.mapToCityDTOs;

@Service
@RequiredArgsConstructor
public class DictCityService {
    private final DictCityRepository dictCityRepository;

    public List<CityDTO> findAllCities() {
        return mapToCityDTOs(dictCityRepository.findAll());
    }

    public Optional<DictCity> findCityByUrnNameIfExists(String urnName) {
        return dictCityRepository.findCityByUrnName(urnName);
    }

    public DictCity findCityByUrnName(String urnName) {
        return dictCityRepository.findCityByUrnName(urnName)
                .orElseThrow(() -> new CityNotFoundException("City [URN name: " + urnName + "] not found"));
    }
}
