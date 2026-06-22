package com.example.person.service;

import com.example.person.model.User;
import com.example.person.model.Weather;
import com.example.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {

	private final PersonRepository repository;
	private final RestTemplate restTemplate;

	public List<User> findAll() {
		List<User> users = new ArrayList<>();
		repository.findAll().forEach(users::add);
		return users;
	}

	public Optional<User> findById(int id) {
		return repository.findById(id);
	}

	public CreateResult create(User user) {
		Optional<User> existing = repository.findById(user.getId());
		if (existing.isPresent()) {
			return new CreateResult(existing.get(), false);
		}
		return new CreateResult(repository.save(user), true);
	}

	public SaveResult<User> saveOrUpdate(int id, User user) {
		return repository.findById(id)
				.map(existing -> {
					existing.setFirstname(user.getFirstname());
					existing.setSurname(user.getSurname());
					existing.setLastname(user.getLastname());
					existing.setBirthday(user.getBirthday());
					existing.setLocation(user.getLocation());
					return new SaveResult<>(repository.save(existing), false);
				})
				.orElseGet(() -> {
					user.setId(id);
					return new SaveResult<>(repository.save(user), true);
				});
	}

	public void delete(int id) {
		repository.deleteById(id);
	}

	public Optional<Weather> getWeather(int id) {
		if (!repository.existsById(id)) {
			return Optional.empty();
		}
		String location = repository.findById(id).get().getLocation();
		Weather weather = restTemplate.getForObject(
				"http://location-service/location/weather?name={name}",
				Weather.class,
				location);
		return Optional.ofNullable(weather);
	}

	public record SaveResult<T>(T entity, boolean created) {
	}

	public record CreateResult(User user, boolean created) {
	}

}
