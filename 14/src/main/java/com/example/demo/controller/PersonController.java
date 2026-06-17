package com.example.demo.controller;

import com.example.demo.dto.Message;
import com.example.demo.dto.Person;
import com.example.demo.service.MessageService;
import com.example.demo.service.PersonService;
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
public class PersonController {

	private final PersonService personService;
	private final MessageService messageService;

	public PersonController(PersonService personService, MessageService messageService) {
		this.personService = personService;
		this.messageService = messageService;
	}

	@GetMapping
	public Iterable<Person> getAll() {
		return personService.getAll();
	}

	@GetMapping("/{id}")
	public Optional<Person> getById(@PathVariable int id) {
		return personService.getById(id);
	}

	@PostMapping
	public Person create(@RequestBody Person person) {
		return personService.create(person);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Person> update(@PathVariable int id, @RequestBody Person person) {
		return personService.update(id, person);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		personService.delete(id);
	}

	@GetMapping("/{p_id}/message")
	public ResponseEntity<List<Message>> getMessages(@PathVariable("p_id") int personId) {
		return messageService.getMessagesByPersonId(personId)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/{p_id}/message/{m_id}")
	public ResponseEntity<Message> getMessage(
			@PathVariable("p_id") int personId,
			@PathVariable("m_id") int messageId) {
		return messageService.getMessageByPersonIdAndMessageId(personId, messageId)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/{p_id}/message")
	public ResponseEntity<Message> addMessage(
			@PathVariable("p_id") int personId,
			@RequestBody Message message) {
		return messageService.addMessageToPerson(personId, message)
				.map(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved))
				.orElseGet(() -> ResponseEntity.badRequest().build());
	}

	@DeleteMapping("/{p_id}/message/{m_id}")
	public ResponseEntity<Void> deleteMessage(
			@PathVariable("p_id") int personId,
			@PathVariable("m_id") int messageId) {
		if (messageService.deleteMessageFromPerson(personId, messageId)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
