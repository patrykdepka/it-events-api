package pl.patrykdepka.iteventsapi.dictionary.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.patrykdepka.iteventsapi.core.BaseIT;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.patrykdepka.iteventsapi.creator.DictCityCreator.createRzeszow;

class DictCityRepositoryTest extends BaseIT {

    @Autowired
    protected DictCityRepository dictCityRepository;

    @Test
    void shouldReturnOptionalOfCityWithGivenUrnNameIfExists() {
        // given
        String urnName = "rzeszow";
        // when
        Optional<DictCity> city = dictCityRepository.findCityByUrnName(urnName);
        // then
        assertThat(city.isPresent()).isTrue();
        assertThat(city.get()).isEqualTo(createRzeszow());
    }

    @Test
    void shouldReturnEmptyOptionalIfCityWithGivenUrnNameDoesNotExists() {
        // given
        String urnName = "krakow";
        // when
        Optional<DictCity> city = dictCityRepository.findCityByUrnName(urnName);
        // then
        assertThat(city.isEmpty()).isTrue();
    }
}
