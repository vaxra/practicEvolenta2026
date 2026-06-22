package com.example.weather.repository;

import com.example.weather.dto.Weather;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WeatherRepository extends CrudRepository<Weather, Integer> {

	Optional<Weather> findByLatitudeAndLongitude(double latitude, double longitude);

}
