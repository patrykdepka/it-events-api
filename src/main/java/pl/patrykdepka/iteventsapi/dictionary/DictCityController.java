package pl.patrykdepka.iteventsapi.dictionary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.patrykdepka.iteventsapi.dictionary.domain.CityDTO;
import pl.patrykdepka.iteventsapi.dictionary.domain.DictCityService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DictCityController {
    private final DictCityService dictCityService;

    @GetMapping("/dictionaries/cities")
    public List<CityDTO> getAllCities() {
        return dictCityService.findAllCities();
    }
}
