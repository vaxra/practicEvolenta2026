package com.example.location.service;

import com.example.location.model.Location;
import com.example.location.model.Weather;
import com.example.location.repository.LocationRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {

	private final LocationRepository repository;
	private final RestTemplate restTemplate;
	private final String weatherServiceUrl;

	public LocationService(LocationRepository repository,
			RestTemplate restTemplate,
			@Value("${weather.service.url}") String weatherServiceUrl) {
		this.repository = repository;
		this.restTemplate = restTemplate;
		this.weatherServiceUrl = weatherServiceUrl;
	}

	public List<Location> findAll(String name) {
		if (name != null) {
			return repository.findByName(name)
					.map(List::of)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		}
		List<Location> locations = new ArrayList<>();
		repository.findAll().forEach(locations::add);
		return locations;
	}

	public Location create(Location location) {
		return repository.save(location);
	}

	public SaveResult<Location> saveOrUpdate(String name, Location location) {
		return repository.findByName(name)
				.map(existing -> {
					existing.setLatitude(location.getLatitude());
					existing.setLongitude(location.getLongitude());
					return new SaveResult<>(repository.save(existing), false);
				})
				.orElseGet(() -> {
					location.setName(name);
					return new SaveResult<>(repository.save(location), true);
				});
	}

	public boolean delete(String name) {
		return repository.findByName(name)
				.map(location -> {
					repository.delete(location);
					return true;
				})
				.orElse(false);
	}

	public Weather getWeather(String name) {
		Location location = repository.findByName(name)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		WeatherResponse response = restTemplate.getForObject(
				weatherServiceUrl + "?lat={lat}&lon={lon}",
				WeatherResponse.class,
				location.getLatitude(),
				location.getLongitude());

		if (response == null || response.main == null) {
			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Не удалось получить данные о погоде");
		}

		String description = response.weather != null && !response.weather.isEmpty()
				? response.weather.get(0).description
				: null;

		return new Weather(response.main.temp, description, response.main.humidity);
	}

	public record SaveResult<T>(T entity, boolean created) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class WeatherResponse {

		public Main main;
		public ArrayList<WeatherItem> weather;

		@JsonIgnoreProperties(ignoreUnknown = true)
		private static class Main {
			public double temp;
			public int humidity;
		}

		@JsonIgnoreProperties(ignoreUnknown = true)
		private static class WeatherItem {
			public String description;
		}

	}

}
