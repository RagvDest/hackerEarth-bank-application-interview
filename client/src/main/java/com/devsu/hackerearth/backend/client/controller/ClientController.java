package com.devsu.hackerearth.backend.client.controller;

import java.net.URI;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;

import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDtoCreate;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.service.ClientService;
import com.devsu.hackerearth.backend.client.mapper.ClientMapper;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/clients")
public class ClientController {
	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);


	private final ClientService clientService;
	private final ClientMapper clientMapper;

	public ClientController(ClientService clientService, ClientMapper clientMapper) {
		this.clientService = clientService;
		this.clientMapper = clientMapper;
	}

	@GetMapping()
	public ResponseEntity<List<ClientDto>> getAll(){
		// api/clients
		// Get all clients
		List<ClientDto> clients = this.clientService.getAll();
		return ResponseEntity.ok(clients);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClientDto> get(@PathVariable Long id){
		// api/clients/{id}
		// Get clients by id
		ClientDto clientFound = this.clientService.getById(id);
		return ResponseEntity.ok(clientFound);
	}

	@PostMapping()
	public ResponseEntity<ClientDto> create(@Valid @RequestBody ClientDtoCreate clientDtoCreate){
		// api/clients
		// Create client
		String winfo = "[create] ";
		ClientDto clientDto = this.clientMapper.createToDTO(clientDtoCreate);


		ClientDto createdClient = this.clientService.create(clientDto);
		logger.info(winfo+"Creado ok: " + createdClient);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(createdClient.getId())
			.toUri();
		
		logger.info(winfo + "Uri generated: "+location.toString());

		return ResponseEntity.created(location).body(createdClient);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClientDto> update(@PathVariable Long id, @RequestBody ClientDto clientDto){
		// api/clients/{id}
		// Update client
		String winfo = "[update] ";
		clientDto.setId(id);
		ClientDto clientUpdated = this.clientService.update(clientDto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.build()
			.toUri();
		
		logger.info(winfo + "Uri generated: "+location.toString());

		return ResponseEntity.ok()
				.header("Content-Location", location.toString())
				.body(clientUpdated);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ClientDto> partialUpdate(@PathVariable Long id, @RequestBody PartialClientDto partialClientDto){
		// api/accounts/{id}
		// Partial update accounts
		String winfo = "[partialUpdate] ";
		ClientDto clientUpdated = this.clientService.partialUpdate(id,partialClientDto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.build()
			.toUri();
		
		logger.info(winfo + "Uri generated: "+location.toString());

		return ResponseEntity.ok()
				.header("Content-Location", location.toString())
				.body(clientUpdated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		// api/clients/{id}
		// Delete client
		String winfo = "[partialUpdate] ";
		this.clientService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
