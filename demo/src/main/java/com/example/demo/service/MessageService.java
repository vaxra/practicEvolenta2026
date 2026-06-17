package com.example.demo.service;

import com.example.demo.dto.Message;
import com.example.demo.dto.Person;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

	private final MessageRepository messageRepository;
	private final PersonRepository personRepository;

	public MessageService(MessageRepository messageRepository, PersonRepository personRepository) {
		this.messageRepository = messageRepository;
		this.personRepository = personRepository;
	}

	public Iterable<Message> getAll() {
		return messageRepository.findAll();
	}

	public Optional<Message> getById(int id) {
		return messageRepository.findById(id);
	}

	public Message create(Message message) {
		if (message.getTime() == null) {
			message.setTime(LocalDateTime.now());
		}
		return messageRepository.save(message);
	}

	public ResponseEntity<Message> update(int id, Message message) {
		return messageRepository.findById(id)
				.map(existing -> {
					existing.setTitle(message.getTitle());
					existing.setText(message.getText());
					existing.setTime(message.getTime() != null ? message.getTime() : existing.getTime());
					return ResponseEntity.ok(messageRepository.save(existing));
				})
				.orElseGet(() -> {
					message.setId(id);
					if (message.getTime() == null) {
						message.setTime(LocalDateTime.now());
					}
					return new ResponseEntity<>(messageRepository.save(message), HttpStatus.CREATED);
				});
	}

	public void delete(int id) {
		messageRepository.deleteById(id);
	}

	public Optional<List<Message>> getMessagesByPersonId(int personId) {
		if (!personRepository.existsById(personId)) {
			return Optional.empty();
		}
		return Optional.of(messageRepository.findByPersonId(personId));
	}

	public Optional<Message> getMessageByPersonIdAndMessageId(int personId, int messageId) {
		if (!personRepository.existsById(personId)) {
			return Optional.empty();
		}
		return messageRepository.findByIdAndPersonId(messageId, personId);
	}

	public Optional<Message> addMessageToPerson(int personId, Message message) {
		Optional<Person> person = personRepository.findById(personId);
		if (person.isEmpty()) {
			return Optional.empty();
		}
		message.setPerson(person.get());
		if (message.getTime() == null) {
			message.setTime(LocalDateTime.now());
		}
		Message saved = messageRepository.save(message);
		person.get().getMesages().add(saved);
		return Optional.of(saved);
	}

	public boolean deleteMessageFromPerson(int personId, int messageId) {
		Optional<Message> message = messageRepository.findByIdAndPersonId(messageId, personId);
		if (message.isEmpty()) {
			return false;
		}
		messageRepository.delete(message.get());
		return true;
	}
}
