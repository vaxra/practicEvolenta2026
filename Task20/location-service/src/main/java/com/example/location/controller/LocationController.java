package com.example.location.controller;

import com.example.location.dto.Location;
import com.example.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

	private final LocationService locationService;

	@GetMapping
	public Iterable<Location> getAll() {
		return locationService.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Location> getById(@PathVariable int id) {
		return locationService.findById(id);
	}

	@GetMapping("/person/{personId}")
	public ResponseEntity<Location> getByPersonId(@PathVariable int personId) {
		return locationService.findByPersonId(personId)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public Location create(@RequestBody Location location) {
		return locationService.create(location);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Location> update(@PathVariable int id, @RequestBody Location location) {
		LocationService.SaveResult<Location> result = locationService.saveOrUpdate(id, location);
		return result.created()
				? new ResponseEntity<>(result.entity(), HttpStatus.CREATED)
				: ResponseEntity.ok(result.entity());
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		locationService.delete(id);
	}

}
