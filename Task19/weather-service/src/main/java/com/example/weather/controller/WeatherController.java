package com.example.weather.controller;

import com.example.weather.dto.Weather;
import com.example.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

	private final WeatherService weatherService;

	@GetMapping
	public ResponseEntity<Weather> getByCoordinates(
			@RequestParam double latitude,
			@RequestParam double longitude) {
		return weatherService.findByCoordinates(latitude, longitude)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

}
