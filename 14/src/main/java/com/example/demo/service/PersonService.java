package com.example.demo.service;

import com.example.demo.dto.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

	private final PersonRepository repository;

	public PersonService(PersonRepository repository) {
		this.repository = repository;
	}

	public Iterable<Person> getAll() {
		return repository.findAll();
	}

	public Optional<Person> getById(int id) {
		return repository.findById(id);
	}

	public Person create(Person person) {
		return repository.save(person);
	}

	public ResponseEntity<Person> update(int id, Person person) {
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

	public void delete(int id) {
		repository.deleteById(id);
	}

	public boolean existsById(int id) {
		return repository.existsById(id);
	}
}
