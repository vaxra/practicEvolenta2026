package com.example.location.service;

import com.example.location.model.Location;
import com.example.location.model.Weather;
<<<<<<< HEAD:Task22/location-service/src/main/java/com/example/location/controller/LocationController.java
import com.example.location.service.LocationService;
import lombok.RequiredArgsConstructor;
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
=======
import com.example.location.repository.LocationRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
>>>>>>> 587826fd5a3a98c79ea69af63fd2e3dc6f8820d5:Task21/location-service/src/main/java/com/example/location/service/LocationService.java

import java.util.List;
import java.util.Optional;

<<<<<<< HEAD:Task22/location-service/src/main/java/com/example/location/controller/LocationController.java
@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

	private final LocationService locationService;

	@GetMapping
	public List<Location> getAll(@RequestParam(required = false) String name) {
		return locationService.findAll(name);
	}

	@PostMapping
	public Location create(@RequestBody Location location) {
		return locationService.create(location);
	}

	@PutMapping
	public ResponseEntity<Location> update(@RequestParam String name, @RequestBody Location location) {
		LocationService.SaveResult<Location> result = locationService.saveOrUpdate(name, location);
		return result.created()
				? new ResponseEntity<>(result.entity(), HttpStatus.CREATED)
				: ResponseEntity.ok(result.entity());
	}

	@DeleteMapping
	public ResponseEntity<Void> delete(@RequestParam String name) {
		return locationService.delete(name)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}

	@GetMapping("/weather")
	public Weather getWeather(@RequestParam String name) {
		return locationService.getWeather(name);
=======
@Service
@RequiredArgsConstructor
public class LocationService {

	private final LocationRepository repository;
	private final RestTemplate restTemplate;

	@Value("${weather.service.url}")
	private String weatherServiceUrl;

	public List<Location> findAll() {
		List<Location> locations = new ArrayList<>();
		repository.findAll().forEach(locations::add);
		return locations;
	}

	public List<Location> findAll(String name) {
		if (name == null) {
			return findAll();
		}
		return repository.findByName(name)
				.map(List::of)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	public Optional<Location> findByName(String name) {
		return repository.findByName(name);
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

	public boolean deleteByName(String name) {
		return repository.findByName(name)
				.map(location -> {
					repository.delete(location);
					return true;
				})
				.orElse(false);
	}

	public Weather getWeatherByName(String name) {
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

>>>>>>> 587826fd5a3a98c79ea69af63fd2e3dc6f8820d5:Task21/location-service/src/main/java/com/example/location/service/LocationService.java
	}

}
