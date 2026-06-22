package com.example.person.service;

import com.example.person.dto.Person;
import com.example.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

	private final PersonRepository repository;

	public Iterable<Person> findAll() {
		return repository.findAll();
	}

	public Optional<Person> findById(int id) {
		return repository.findById(id);
	}

	public Person create(Person person) {
		return repository.save(person);
	}

	public SaveResult<Person> saveOrUpdate(int id, Person person) {
		return repository.findById(id)
				.map(existing -> {
					existing.setFirstname(person.getFirstname());
					existing.setSurname(person.getSurname());
					existing.setLastname(person.getLastname());
					existing.setBirthday(person.getBirthday());
					return new SaveResult<>(repository.save(existing), false);
				})
				.orElseGet(() -> {
					person.setId(id);
					return new SaveResult<>(repository.save(person), true);
				});
	}

	public void delete(int id) {
		repository.deleteById(id);
	}

	public record SaveResult<T>(T entity, boolean created) {
	}

}
