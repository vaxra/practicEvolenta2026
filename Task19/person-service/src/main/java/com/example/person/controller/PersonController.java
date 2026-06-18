package com.example.person.controller;

import com.example.person.dto.Person;
import com.example.person.repository.PersonRepository;
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
@RequestMapping("/person")
public class PersonController {

	private final PersonRepository repository;

	public PersonController(PersonRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public Iterable<Person> getAll() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Person> getById(@PathVariable int id) {
		return repository.findById(id);
	}

	@PostMapping
	public Person create(@RequestBody Person person) {
		return repository.save(person);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Person> update(@PathVariable int id, @RequestBody Person person) {
		return repository.findById(id)
				.map(existing -> {
					existing.setFirstname(person.getFirstname());
					existing.setSurname(person.getSurname());
					existing.setLastname(person.getLastname());
					existing.setBirthday(person.getBirthday());
					return ResponseEntity.ok(repository.save(existing));
				})
				.orElseGet(() -> {
					person.setId(id);
					return new ResponseEntity<>(repository.save(person), HttpStatus.CREATED);
				});
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		repository.deleteById(id);
	}

}
