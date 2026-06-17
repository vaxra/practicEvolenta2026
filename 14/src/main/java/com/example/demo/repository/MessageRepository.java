package com.example.demo.repository;

import com.example.demo.dto.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {

	List<Message> findByPersonId(int personId);

	Optional<Message> findByIdAndPersonId(int id, int personId);
}
