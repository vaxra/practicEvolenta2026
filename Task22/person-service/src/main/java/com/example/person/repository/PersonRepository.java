package com.example.person.repository;

import com.example.person.model.User;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<User, Integer> {
}
