package com.example.location.controller;

import com.example.location.model.Location;
import com.example.location.model.Weather;
import com.example.location.service.LocationService;
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

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

	private final LocationService locationService;

	public LocationController(LocationService locationService) {
		this.locationService = locationService;
	}

	@GetMapping
	public List<Location> getAll(@RequestParam(required = false) String name) {
		return locationService.getAll(name);
	}

	@PostMapping
	public Location create(@RequestBody Location location) {
		return locationService.create(location);
	}

	@PutMapping
	public ResponseEntity<Location> update(@RequestParam String name, @RequestBody Location location) {
		LocationService.LocationUpdateResult result = locationService.update(name, location);
		return result.created()
				? new ResponseEntity<>(result.location(), HttpStatus.CREATED)
				: ResponseEntity.ok(result.location());
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
	}

}
