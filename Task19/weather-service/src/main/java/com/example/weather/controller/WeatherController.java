package com.example.weather.controller;

import com.example.weather.dto.Weather;
import com.example.weather.repository.WeatherRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

	private final WeatherRepository repository;

	public WeatherController(WeatherRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public ResponseEntity<Weather> getByCoordinates(
			@RequestParam double latitude,
			@RequestParam double longitude) {
		return repository.findByLatitudeAndLongitude(latitude, longitude)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

}
