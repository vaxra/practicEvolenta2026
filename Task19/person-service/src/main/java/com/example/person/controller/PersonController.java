package com.example.person.controller;

import com.example.person.dto.Person;
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

import java.util.Optional;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

	private final PersonService personService;

	@GetMapping
	public Iterable<Person> getAll() {
		return personService.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Person> getById(@PathVariable int id) {
		return personService.findById(id);
	}

	@PostMapping
	public Person create(@RequestBody Person person) {
		return personService.create(person);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Person> update(@PathVariable int id, @RequestBody Person person) {
		PersonService.SaveResult<Person> result = personService.saveOrUpdate(id, person);
		return result.created()
				? new ResponseEntity<>(result.entity(), HttpStatus.CREATED)
				: ResponseEntity.ok(result.entity());
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		personService.delete(id);
	}

}
