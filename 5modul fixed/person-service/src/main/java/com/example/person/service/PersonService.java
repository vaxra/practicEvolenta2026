package com.example.person.service;

import com.example.person.model.User;
import com.example.person.model.Weather;
import com.example.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

	private final PersonRepository repository;
	private final RestTemplate restTemplate;
	private final String locationServiceUrl;

	public PersonService(PersonRepository repository,
			RestTemplate restTemplate,
			@Value("${location.service.url}") String locationServiceUrl) {
		this.repository = repository;
		this.restTemplate = restTemplate;
		this.locationServiceUrl = locationServiceUrl;
	}

	public Optional<Weather> getWeather(int id) {
		if (!repository.existsById(id)) {
			return Optional.empty();
		}
		String location = repository.findById(id).get().getLocation();
		Weather weather = restTemplate.getForObject(locationServiceUrl, Weather.class, location);
		return Optional.ofNullable(weather);
	}

	public List<User> getAll() {
		List<User> users = new ArrayList<>();
		repository.findAll().forEach(users::add);
		return users;
	}

	public Optional<User> getById(int id) {
		return repository.findById(id);
	}

	public CreateUserResult create(User user) {
		if (repository.findById(user.getId()).isPresent()) {
			return new CreateUserResult(repository.findById(user.getId()).get(), true);
		}
		return new CreateUserResult(repository.save(user), false);
	}

	public UserUpdateResult update(int id, User user) {
		return repository.findById(id)
				.map(existing -> {
					existing.setFirstname(user.getFirstname());
					existing.setSurname(user.getSurname());
					existing.setLastname(user.getLastname());
					existing.setBirthday(user.getBirthday());
					existing.setLocation(user.getLocation());
					return new UserUpdateResult(repository.save(existing), false);
				})
				.orElseGet(() -> {
					user.setId(id);
					return new UserUpdateResult(repository.save(user), true);
				});
	}

	public void delete(int id) {
		repository.deleteById(id);
	}

	public record CreateUserResult(User user, boolean conflict) {
	}

	public record UserUpdateResult(User user, boolean created) {
	}

}
