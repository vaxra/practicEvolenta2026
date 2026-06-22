package com.example.location.controller;

import com.example.location.model.Location;
import com.example.location.model.Weather;
import com.example.location.repository.LocationRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

	private final LocationRepository repository;
	private final RestTemplate restTemplate;
	private final String weatherServiceUrl;

	public LocationController(LocationRepository repository,
			RestTemplate restTemplate,
			@Value("${weather.service.url}") String weatherServiceUrl) {
		this.repository = repository;
		this.restTemplate = restTemplate;
		this.weatherServiceUrl = weatherServiceUrl;
	}

	@GetMapping
	public List<Location> getAll(@RequestParam(required = false) String name) {
		if (name != null) {
			return repository.findByName(name)
					.map(List::of)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		}
		List<Location> locations = new ArrayList<>();
		repository.findAll().forEach(locations::add);
		return locations;
	}

	@PostMapping
	public Location create(@RequestBody Location location) {
		return repository.save(location);
	}

	@PutMapping
	public ResponseEntity<Location> update(@RequestParam String name, @RequestBody Location location) {
		return repository.findByName(name)
				.map(existing -> {
					existing.setLatitude(location.getLatitude());
					existing.setLongitude(location.getLongitude());
					return ResponseEntity.ok(repository.save(existing));
				})
				.orElseGet(() -> {
					location.setName(name);
					return new ResponseEntity<>(repository.save(location), HttpStatus.CREATED);
				});
	}

	@DeleteMapping
	public ResponseEntity<Void> delete(@RequestParam String name) {
		return repository.findByName(name)
				.map(location -> {
					repository.delete(location);
					return ResponseEntity.noContent().<Void>build();
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/weather")
	public Weather getWeather(@RequestParam String name) {
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
