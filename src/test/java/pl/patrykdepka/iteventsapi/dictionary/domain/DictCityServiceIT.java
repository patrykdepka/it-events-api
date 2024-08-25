package pl.patrykdepka.iteventsapi.dictionary.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.patrykdepka.iteventsapi.core.BaseIT;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.patrykdepka.iteventsapi.creator.DictCityCreator.createRzeszow;
import static pl.patrykdepka.iteventsapi.creator.DictCityCreator.createRzeszowCityDto;
import static pl.patrykdepka.iteventsapi.creator.DictCityCreator.createWarsawCityDto;

class DictCityServiceIT extends BaseIT {

    @Autowired
    private DictCityService dictCityService;

    @Test
    void shouldReturnAllCities() {
        // given
        List<CityDTO> cities = List.of(createRzeszowCityDto(), createWarsawCityDto());
        // when
        List<CityDTO> returnedCities = dictCityService.findAllCities();
        // then
        assertThat(returnedCities.size()).isEqualTo(2);
        assertThat(returnedCities.containsAll(cities)).isTrue();
    }

    @Test
    void shouldReturnOptionalOfCityWithGivenUrnNameIfExists() {
        // given
        String urnName = "rzeszow";
        // when
        Optional<DictCity> city = dictCityService.findCityByUrnNameIfExists(urnName);
        // then
        assertThat(city.isPresent()).isTrue();
        assertThat(city.get()).isEqualTo(createRzeszow());
    }

    @Test
    void shouldReturnEmptyOptionalIfCityWithGivenUrnNameDoesNotExists() {
        // given
        String urnName = "krakow";
        // when
        Optional<DictCity> city = dictCityService.findCityByUrnNameIfExists(urnName);
        // then
        assertThat(city.isEmpty()).isTrue();
    }

    @Test
    void shouldReturnCityWithGivenUrnName() {
        // given
        String urnName = "rzeszow";
        // when
        DictCity city = dictCityService.findCityByUrnName(urnName);
        // then
        assertThat(city).isEqualTo(createRzeszow());
    }
}
