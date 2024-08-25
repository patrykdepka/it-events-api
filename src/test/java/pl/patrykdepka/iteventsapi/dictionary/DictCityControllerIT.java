package pl.patrykdepka.iteventsapi.dictionary;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import pl.patrykdepka.iteventsapi.core.BaseControllerIT;
import pl.patrykdepka.iteventsapi.dictionary.domain.CityDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static pl.patrykdepka.iteventsapi.creator.DictCityCreator.createRzeszowCityDto;
import static pl.patrykdepka.iteventsapi.creator.DictCityCreator.createWarsawCityDto;

class DictCityControllerIT extends BaseControllerIT {

    @Test
    void shouldReturnAllCities() {
        // given
        List<CityDTO> cities = List.of(createRzeszowCityDto(), createWarsawCityDto());
        // when
        ResponseEntity<List<CityDTO>> response = restTemplate.exchange(
                "/api/v1/dictionaries/cities",
                GET,
                sendRequestAsUser(null),
                new ParameterizedTypeReference<>() {}
        );
        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().containsAll(cities)).isTrue();
    }
}