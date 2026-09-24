package com.devsu.hackerearth.backend.client.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;
import com.devsu.hackerearth.backend.client.mapper.ClientMapper;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.external.account.AccountExternalPort;
import com.devsu.hackerearth.backend.client.external.account.AccountCheckResponse;

import com.devsu.hackerearth.backend.client.exception.ResourceNotFoundException;
import com.devsu.hackerearth.backend.client.exception.DuplicateResourceException;
import com.devsu.hackerearth.backend.client.exception.NotValidException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ClientServiceImpl implements ClientService {

	private static final Logger logger = LoggerFactory.getLogger(ClientService.class);


	private final ClientRepository clientRepository;
	private final ClientMapper clientMapper;
	private final PasswordEncoder passwordEncoder;
	private final AccountExternalPort accountPort;

	public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper,
		PasswordEncoder passwordEncoder, AccountExternalPort accountPort
	) {
		this.clientRepository = clientRepository;
		this.clientMapper = clientMapper;
		this.passwordEncoder = passwordEncoder;
		this.accountPort = accountPort;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ClientDto> getAll() {
		// Get all clients
		List<Client> clients = this.clientRepository.findAll();
		return this.clientMapper.toDtoList(clients);
	}

	@Override
	@Transactional(readOnly = true)
	public ClientDto getById(Long id) {
		// Get clients by id
		return this.clientRepository.findById(id)
		.map(this.clientMapper::entityToDTO)
		.orElseThrow(()-> new ResourceNotFoundException("Client not found"));
	}

	@Override
	@Transactional
	public ClientDto create(ClientDto clientDto) {
		String winfo = "[create] ";
		// Create client 
		logger.info(winfo +"clientDto ini: "+ clientDto);
		if (this.clientRepository.existsByDni(clientDto.getDni())){
			throw new DuplicateResourceException("Ya existe cliente con el DNI: "+clientDto.getDni());
		}
		// Hash password
		clientDto.setPassword(this.passwordEncoder.encode(clientDto.getPassword()));
		// Save Entity
		Client entity = this.clientMapper.dtoToEntity(clientDto);
		Client saved = this.clientRepository.save(entity);

		return this.clientMapper.entityToDTO(saved);
	}

	@Override
	@Transactional
	public ClientDto update(ClientDto clientDto) {
		// Update client
		Client client = this.clientRepository.findById(clientDto.getId()).orElseThrow(()-> new ResourceNotFoundException("Cliente no encontrado"));

		this.clientMapper.updateClientFromDto(clientDto, client);
		Client clientUpdated = this.clientRepository.save(client);
		return this.clientMapper.entityToDTO(clientUpdated);
	}

	@Override
	@Transactional
    public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) {
        // Partial update account
		Client client = this.clientRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Cliente no encontrado"));

		this.clientMapper.updateClientFromPartialDto(partialClientDto, client);
		Client clientUpdated = this.clientRepository.save(client);
		return this.clientMapper.entityToDTO(clientUpdated);
    }

	@Override
	@Transactional
	public void deleteById(Long id) {
		// Delete client
		String winfo = "[delete] ";
		logger.info(winfo +"ID: "+ id);
		if(!this.clientRepository.existsById(id)){
			throw new ResourceNotFoundException("Client not found");
		}
	
		if(this.accountPort.checkClientHasAccounts(id)){
			throw new NotValidException("Can't delete client with accounts");
		}
		logger.info(winfo +"Procced to delete: "+ id);
		this.clientRepository.deleteById(id);
	}
}
