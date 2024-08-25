package pl.patrykdepka.iteventsapi.dictionary.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DictCityRepository extends JpaRepository<DictCity, Long> {

    Optional<DictCity> findCityByUrnName(String urnName);
}
