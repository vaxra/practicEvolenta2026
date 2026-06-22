package com.example.weather.config;

import com.example.weather.service.WeatherService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherDataInitializer {

	@Bean
	CommandLineRunner initWeatherData(WeatherService weatherService) {
		return args -> weatherService.initDefaultDataIfEmpty();
	}

}
