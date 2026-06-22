package com.example.location.controller;

import com.example.location.model.Location;
import com.example.location.model.Weather;
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

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

	private final LocationService locationService;

	@GetMapping
	public Object getAll(@RequestParam(required = false) String name) {
		if (name == null) {
			return locationService.findAll();
		}
		return locationService.getByName(name);
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
		return locationService.deleteByName(name)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}

	@GetMapping("/weather")
	public Weather getWeather(@RequestParam String name) {
		return locationService.getWeatherByName(name);
	}

}
