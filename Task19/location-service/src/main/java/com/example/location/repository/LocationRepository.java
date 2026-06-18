package com.example.location.repository;

import com.example.location.dto.Location;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LocationRepository extends CrudRepository<Location, Integer> {

	Optional<Location> findByPersonId(int personId);

}
