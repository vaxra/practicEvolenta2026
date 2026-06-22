package com.example.location.service;

import com.example.location.dto.Location;
import com.example.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

	private final LocationRepository repository;

	public Iterable<Location> findAll() {
		return repository.findAll();
	}

	public Optional<Location> findById(int id) {
		return repository.findById(id);
	}

	public Optional<Location> findByPersonId(int personId) {
		return repository.findByPersonId(personId);
	}

	public Location create(Location location) {
		return repository.save(location);
	}

	public SaveResult<Location> saveOrUpdate(int id, Location location) {
		return repository.findById(id)
				.map(existing -> {
					existing.setPersonId(location.getPersonId());
					existing.setCity(location.getCity());
					existing.setLatitude(location.getLatitude());
					existing.setLongitude(location.getLongitude());
					return new SaveResult<>(repository.save(existing), false);
				})
				.orElseGet(() -> {
					location.setId(id);
					return new SaveResult<>(repository.save(location), true);
				});
	}

	public void delete(int id) {
		repository.deleteById(id);
	}

	public record SaveResult<T>(T entity, boolean created) {
	}

}
