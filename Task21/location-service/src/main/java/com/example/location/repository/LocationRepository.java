package com.example.location.repository;

import com.example.location.model.Location;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LocationRepository extends CrudRepository<Location, String> {

	Optional<Location> findByName(String name);

}
