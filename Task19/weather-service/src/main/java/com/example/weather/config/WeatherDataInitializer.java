package com.example.weather.config;

import com.example.weather.dto.Weather;
import com.example.weather.repository.WeatherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherDataInitializer {

	@Bean
	CommandLineRunner initWeatherData(WeatherRepository repository) {
		return args -> {
			if (repository.count() == 0) {
				Weather moscow = new Weather();
				moscow.setLatitude(55.7558);
				moscow.setLongitude(37.6173);
				moscow.setTemperature(+20.0);
				moscow.setDescription("Облачно");
				moscow.setHumidity(78);
				repository.save(moscow);

				Weather saransk = new Weather();
                saransk.setLatitude(54.1838);
                saransk.setLongitude(45.1749);
                saransk.setTemperature(+18.0);
                saransk.setDescription("Облачно");
                saransk.setHumidity(85);
				repository.save(saransk);
			}
		};
	}

}
