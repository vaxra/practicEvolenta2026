package com.example.person.controller;

import com.example.person.model.User;
import com.example.person.model.Weather;
import com.example.person.service.PersonService;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

	private final PersonService personService;

	@GetMapping("/{id}/weather")
	public ResponseEntity<Weather> getWeather(@PathVariable int id) {
		return personService.getWeather(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public List<User> getAll() {
		return personService.findAll();
	}

	@GetMapping("/{id}")
	public Optional<User> getById(@PathVariable int id) {
		return personService.findById(id);
	}

	@PostMapping
	public ResponseEntity<User> create(@RequestBody User user) {
		PersonService.CreateResult result = personService.create(user);
		return result.created()
				? new ResponseEntity<>(result.user(), HttpStatus.CREATED)
				: new ResponseEntity<>(result.user(), HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user) {
		PersonService.SaveResult<User> result = personService.saveOrUpdate(id, user);
		return result.created()
				? new ResponseEntity<>(result.entity(), HttpStatus.CREATED)
				: ResponseEntity.ok(result.entity());
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		personService.delete(id);
	}

}
