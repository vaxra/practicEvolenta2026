package com.example.demo.controller;

import com.example.demo.dto.Message;
import com.example.demo.service.MessageService;
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
@RequestMapping("/message")
public class MessageController {

	private final MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@GetMapping
	public Iterable<Message> getAll() {
		return messageService.getAll();
	}

	@GetMapping("/{id}")
	public Optional<Message> getById(@PathVariable int id) {
		return messageService.getById(id);
	}

	@PostMapping
	public Message create(@RequestBody Message message) {
		return messageService.create(message);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Message> update(@PathVariable int id, @RequestBody Message message) {
		return messageService.update(id, message);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		messageService.delete(id);
	}
}
