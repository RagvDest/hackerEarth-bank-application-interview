package com.devsu.hackerearth.backend.account.controller;

import java.util.Date;
import java.util.List;

import java.net.URI;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;

import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.service.TransactionService;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    
    private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@GetMapping()
    public ResponseEntity<List<TransactionDto>> getAll(){
		// api/transactions
		// Get all transactions
		List<TransactionDto> transaction = this.transactionService.getAll();
		return ResponseEntity.ok(transaction);
	}

	@GetMapping("/{id}")
    public ResponseEntity<TransactionDto> get(@PathVariable Long id){
		// api/transactions/{id}
		// Get transactions by id
		TransactionDto transactionFound = this.transactionService.getById(id);
		return ResponseEntity.ok(transactionFound);
	}

	@PostMapping()
	public ResponseEntity<TransactionDto> create(@Valid @RequestBody TransactionDto transactionDto){
		// api/transactions
		// Create transactions
		String winfo = "[create] ";
		transactionDto.setId(null);

		TransactionDto createdTransaction = this.transactionService.create(transactionDto);
		logger.info(winfo+"Creado ok: " + createdTransaction);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(createdTransaction.getId())
			.toUri();
		
		logger.info(winfo + "Uri generated: "+location.toString());

		return ResponseEntity.created(location).body(createdTransaction);
	}

	@GetMapping("/clients/{clientId}/report")
    public ResponseEntity<List<BankStatementDto>> report(@PathVariable Long clientId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTransactionStart, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTransactionEnd) {
		// api/transactions/clients/{clientId}/report
        // Get report
		List<BankStatementDto> report = this.transactionService.getAllByAccountClientIdAndDateBetween(clientId, dateTransactionStart, dateTransactionEnd);

        return ResponseEntity.ok(report);
	}
}
