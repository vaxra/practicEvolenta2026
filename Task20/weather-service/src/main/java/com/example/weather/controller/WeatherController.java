package com.example.weather.controller;

import com.example.weather.model.Root;
import com.example.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
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
	public Root getWeather(@RequestParam double lat, @RequestParam double lon) {
		return weatherService.getWeather(lat, lon);
	}

}
