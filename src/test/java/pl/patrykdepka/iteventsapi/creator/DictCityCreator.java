package pl.patrykdepka.iteventsapi.creator;

import pl.patrykdepka.iteventsapi.dictionary.domain.CityDTO;
import pl.patrykdepka.iteventsapi.dictionary.domain.DictCity;

public class DictCityCreator {

    public static DictCity createRzeszow() {
        return DictCity.builder()
                .id(1L)
                .uuid("fa07d868-6f06-4603-8a2e-8900ccc93c7a")
                .urnName("rzeszow")
                .displayName("Rzeszów")
                .build();
    }

    public static CityDTO createRzeszowCityDto() {
        return new CityDTO("rzeszow", "Rzeszów");
    }

    public static CityDTO createWarsawCityDto() {
        return new CityDTO("warszawa", "Warszawa");
    }
}
