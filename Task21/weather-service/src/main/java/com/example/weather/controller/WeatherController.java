package com.example.weather.controller;

import com.example.weather.config.WeatherConfig;
import com.example.weather.model.Root;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

	private final WeatherConfig.WeatherApiService weatherApiService;

	public WeatherController(WeatherConfig.WeatherApiService weatherApiService) {
		this.weatherApiService = weatherApiService;
	}

	@GetMapping
	public Root getWeather(@RequestParam double lat, @RequestParam double lon) {
		return weatherApiService.getWeather(lat, lon);
	}

}
