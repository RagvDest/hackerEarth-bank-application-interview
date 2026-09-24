package com.devsu.hackerearth.backend.account.controller;

import java.util.List;
import java.util.Map;
import java.net.URI;

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

import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.service.AccountService;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping()
	public ResponseEntity<List<AccountDto>> getAll(){
		// api/accounts
		// Get all accounts
		List<AccountDto> accounts = this.accountService.getAll();
		return ResponseEntity.ok(accounts);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> get(@PathVariable Long id){
		// api/accounts/{id}
		// Get accounts by id
		AccountDto accountFound = this.accountService.getById(id);
		return ResponseEntity.ok(accountFound);
	}

	@GetMapping("/client/{clientId}/exists")
	public ResponseEntity<Map<String, Boolean>> hasActiveAccounts(@PathVariable Long clientId) {
		boolean exists = accountService.existsByClientId(clientId);
		return ResponseEntity.ok(Map.of("hasAccounts", exists));
	}

	@PostMapping()
	public ResponseEntity<AccountDto> create(@Valid @RequestBody AccountDto accountDto){
		// api/accounts
		// Create accounts
		String winfo = "[create] ";
		accountDto.setId(null);

		AccountDto createdAccount = this.accountService.create(accountDto);
		logger.info(winfo+"Creado ok: " + createdAccount);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(createdAccount.getId())
			.toUri();
		
		logger.info(winfo + "Uri generated: "+location.toString());

		return ResponseEntity.created(location).body(createdAccount);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AccountDto> update(@Valid @PathVariable Long id, @RequestBody AccountDto accountDto){
		// api/accounts/{id}
		// Update accounts
		String winfo = "[update] ";
		accountDto.setId(id);
		AccountDto accountUpdated = this.accountService.update(accountDto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.build()
			.toUri();
		
		logger.info(winfo + "Uri generated: "+location.toString());

		return ResponseEntity.ok()
				.header("Content-Location", location.toString())
				.body(accountUpdated);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<AccountDto> partialUpdate(@PathVariable Long id, @Valid@RequestBody PartialAccountDto partialAccountDto){
		// api/accounts/{id}
		// Partial update accounts
		String winfo = "[partialUpdate] ";
		AccountDto accountUpdated = this.accountService.partialUpdate(id,partialAccountDto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.build()
			.toUri();
		
		logger.info(winfo + "Uri generated: "+location.toString());

		return ResponseEntity.ok()
				.header("Content-Location", location.toString())
				.body(accountUpdated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		// api/accounts/{id}
		// Delete accounts
		this.accountService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}

