package com.example.person.controller;

import com.example.person.model.User;
import com.example.person.model.Weather;
import com.example.person.repository.PersonRepository;
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
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

	private final PersonRepository repository;
	private final RestTemplate restTemplate;

	@GetMapping("/{id}/weather")
	public ResponseEntity<Weather> getWeather(@PathVariable int id) {
		if (repository.existsById(id)) {
			String location = repository.findById(id).get().getLocation();
			Weather weather = restTemplate.getForObject(
					"http://location-service/location/weather?name={name}",
					Weather.class,
					location);
			return ResponseEntity.ok(weather);
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping
	public List<User> getAll() {
		List<User> users = new ArrayList<>();
		repository.findAll().forEach(users::add);
		return users;
	}

	@GetMapping("/{id}")
	public Optional<User> getById(@PathVariable int id) {
		return repository.findById(id);
	}

	@PostMapping
	public ResponseEntity<User> create(@RequestBody User user) {
		return repository.findById(user.getId()).isPresent()
				? new ResponseEntity<>(repository.findById(user.getId()).get(), HttpStatus.BAD_REQUEST)
				: new ResponseEntity<>(repository.save(user), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user) {
		return repository.findById(id)
				.map(existing -> {
					existing.setFirstname(user.getFirstname());
					existing.setSurname(user.getSurname());
					existing.setLastname(user.getLastname());
					existing.setBirthday(user.getBirthday());
					existing.setLocation(user.getLocation());
					return ResponseEntity.ok(repository.save(existing));
				})
				.orElseGet(() -> {
					user.setId(id);
					return new ResponseEntity<>(repository.save(user), HttpStatus.CREATED);
				});
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		repository.deleteById(id);
	}

}
