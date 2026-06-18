package com.example.location.controller;

import com.example.location.dto.Location;
import com.example.location.repository.LocationRepository;
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
public class LocationController {

	private final LocationRepository repository;

	public LocationController(LocationRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public Iterable<Location> getAll() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Location> getById(@PathVariable int id) {
		return repository.findById(id);
	}

	@GetMapping("/person/{personId}")
	public ResponseEntity<Location> getByPersonId(@PathVariable int personId) {
		return repository.findByPersonId(personId)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public Location create(@RequestBody Location location) {
		return repository.save(location);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Location> update(@PathVariable int id, @RequestBody Location location) {
		return repository.findById(id)
				.map(existing -> {
					existing.setPersonId(location.getPersonId());
					existing.setCity(location.getCity());
					existing.setLatitude(location.getLatitude());
					existing.setLongitude(location.getLongitude());
					return ResponseEntity.ok(repository.save(existing));
				})
				.orElseGet(() -> {
					location.setId(id);
					return new ResponseEntity<>(repository.save(location), HttpStatus.CREATED);
				});
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		repository.deleteById(id);
	}

}
