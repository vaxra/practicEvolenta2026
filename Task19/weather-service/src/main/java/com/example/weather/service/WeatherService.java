package com.example.weather.service;

import com.example.weather.dto.Weather;
import com.example.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherService {

	private final WeatherRepository repository;

	public Optional<Weather> findByCoordinates(double latitude, double longitude) {
		return repository.findByLatitudeAndLongitude(latitude, longitude);
	}

	public void initDefaultDataIfEmpty() {
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
	}

}
