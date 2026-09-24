package com.devsu.hackerearth.backend.account.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Lazy;

import com.devsu.hackerearth.backend.account.model.Transaction;;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

import com.devsu.hackerearth.backend.account.external.client.ClientExternalPort;
import com.devsu.hackerearth.backend.account.external.client.ClientExternalDto;

import com.devsu.hackerearth.backend.account.exception.ResourceNotFoundException;
import com.devsu.hackerearth.backend.account.exception.NotValidException;

import com.devsu.hackerearth.backend.account.mapper.TransactionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

	private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final TransactionMapper transactionMapper;
    private final ClientExternalPort clientPort;

	public TransactionServiceImpl(TransactionRepository transactionRepository, @Lazy AccountService accountService,
        TransactionMapper transactionMapper, ClientExternalPort clientPort
    ) {
		this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.transactionMapper = transactionMapper;
        this.clientPort = clientPort;
	}

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getAll() {
        // Get all transactions
		List<Transaction> transactions = this.transactionRepository.findAll();
		return this.transactionMapper.toDtoList(transactions);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDto getById(Long id) {
        // Get transactions by id
		return this.transactionRepository.findById(id)
            .map(this.transactionMapper::entityToDTO)
            .orElseThrow(()-> new ResourceNotFoundException("Transaction not found"));
    }

    @Override
    @Transactional
    public TransactionDto create(TransactionDto transactionDto) {
        // Create transaction
		String winfo = "[create] ";
        logger.info(winfo +"TransactionDto ini: "+ transactionDto);
        // Validate Transaction
        this.validateTransaction(transactionDto);

        AccountDto accountDto = new AccountDto();
        try{
            accountDto = this.accountService.getById(transactionDto.getAccountId());
        }catch (ResourceNotFoundException ex){
            throw new NotValidException("Account doesn't exist");
        }
        if(!accountDto.isActive()){
            throw new NotValidException("Account must be active");
        }
        
        //Validate Balance
        TransactionDto lastTransactionDto = this.getLastByAccountId(transactionDto.getAccountId());
        double actualBalance = lastTransactionDto.getId() == null ? accountDto.getInitialAmount() : lastTransactionDto.getBalance();
        
        if(transactionDto.getType().equals("-")){
            if(Math.abs(transactionDto.getAmount()) > actualBalance){
                throw new NotValidException("Saldo no disponible");
            }
        }
        
        // Set values
        double nextBalance = actualBalance + transactionDto.getAmount();
        Date now = Date.from(Instant.now());
        
        transactionDto.setDate(now);
        transactionDto.setBalance(nextBalance);

        Transaction entity = this.transactionMapper.dtoToEntity(transactionDto);
        Transaction saved = this.transactionRepository.save(entity);

		return this.transactionMapper.entityToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankStatementDto> getAllByAccountClientIdAndDateBetween(Long clientId, Date dateTransactionStart,
            Date dateTransactionEnd) {
        // Report
        ClientExternalDto client = this.clientPort.getClientById(clientId)
                .orElseThrow(()-> new ResourceNotFoundException("Client not found"));

        List<AccountDto> accountsDto = this.accountService.getByClientId(client.getId());

        List<BankStatementDto> rows = accountsDto.stream()
                .flatMap(account -> {
                    List<TransactionDto> transactions = this.getTransactionsByAccountIdBetweenDates(account.getId(), dateTransactionStart, dateTransactionEnd);

                    return transactions.stream()
                        .map(tx -> new BankStatementDto(
                            tx.getDate(),
                            client.getName(),
                            account.getNumber(),
                            account.getType(),
                            account.getInitialAmount(),
                            account.isActive(),
                            tx.getType(),
                            tx.getAmount(),
                            tx.getBalance()
                        ));
                })
                .collect(Collectors.toList());

		return rows;
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDto getLastByAccountId(Long accountId) {
        // If you need it
        return this.transactionRepository.findFirstByAccountIdOrderByDateDesc(accountId)
            .map(this.transactionMapper::entityToDTO)
            .orElseGet(TransactionDto::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByAccountIdBetweenDates(Long accountId, Date startDate, Date endDate){
        List<Transaction> transactions = this.transactionRepository.findByAccountIdAndDateBetweenOrderByDateDesc(accountId, startDate, endDate);
        return this.transactionMapper.toDtoList(transactions);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByAccountId(Long accountId){
        return this.transactionRepository.existsByAccountId(accountId);
    }

    private void validateTransaction(TransactionDto transactionDto){
        // Coherencia entre tipo y amount
        if(transactionDto.getType().equals("+") && transactionDto.getAmount() < 0.0){
            throw new NotValidException("Amount must not be negative");
        }
        if(transactionDto.getType().equals("-") && transactionDto.getAmount() > 0.0){
            throw new NotValidException("Amount must not be positive");
        }
    }
}
