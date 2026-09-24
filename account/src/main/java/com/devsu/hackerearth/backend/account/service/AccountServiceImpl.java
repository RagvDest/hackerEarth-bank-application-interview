package com.devsu.hackerearth.backend.account.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.mapper.AccountMapper;
import com.devsu.hackerearth.backend.account.model.Account;

import com.devsu.hackerearth.backend.account.exception.ResourceNotFoundException;
import com.devsu.hackerearth.backend.account.exception.NotValidException;
import com.devsu.hackerearth.backend.account.exception.DuplicateResourceException;

import com.devsu.hackerearth.backend.account.external.client.ClientExternalPort;
import com.devsu.hackerearth.backend.account.external.client.ClientExternalDto;

import com.devsu.hackerearth.backend.account.service.TransactionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

	private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ClientExternalPort clientPort;
    private final TransactionService transactionService;

	public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper,
            ClientExternalPort clientPort, TransactionService transactionService
    ) {
		this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.clientPort = clientPort;
        this.transactionService = transactionService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAll() {
        // Get all accounts
        List<Account> accounts = this.accountRepository.findAll();
		return this.accountMapper.toDtoList(accounts);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto getById(Long id) {
        // Get accounts by id
        return this.accountRepository.findById(id)
            .map(this.accountMapper::entityToDTO)
            .orElseThrow(()-> new ResourceNotFoundException("Account not found"));
    }

    @Override
    @Transactional
    public AccountDto create(AccountDto accountDto) {
        String winfo = "[create] ";
		// Create account 
		logger.info(winfo +"AccountDto ini: "+ accountDto);
		if (this.accountRepository.existsByNumber(accountDto.getNumber())){
			throw new DuplicateResourceException("Account number already exists: "+accountDto.getNumber());
		}
        ClientExternalDto client = this.clientPort.getClientByIdAndisActive(accountDto.getClientId());

        logger.info(winfo +"ClientExternalDto: "+ client);

        Account entity = this.accountMapper.dtoToEntity(accountDto);
        Account saved = this.accountRepository.save(entity);

		return this.accountMapper.entityToDTO(saved);
    }

    @Override
    @Transactional
    public AccountDto update(AccountDto accountDto) {
        // Update account
		Account account = this.accountRepository.findById(accountDto.getId()).orElseThrow(()-> new ResourceNotFoundException("Account not found"));

        if(!account.getClientId().equals(accountDto.getClientId())){
            throw new NotValidException("Client can't be changed");
        }

        if(!account.getNumber().equals(accountDto.getNumber())){
            throw new NotValidException("Account Number can't be changed");
        }

        ClientExternalDto client = this.clientPort.getClientByIdAndisActive(accountDto.getClientId());

		this.accountMapper.updateAccountFromDto(accountDto, account);
		Account accountUpdated = this.accountRepository.save(account);
		return this.accountMapper.entityToDTO(accountUpdated);
	}

    @Override
    @Transactional
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        // Partial update account
		Account account = this.accountRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Account not found"));

        ClientExternalDto client = this.clientPort.getClientByIdAndisActive(account.getClientId());

		this.accountMapper.updateAccountFromPartialDto(partialAccountDto, account);
		Account accountUpdated = this.accountRepository.save(account);
		return this.accountMapper.entityToDTO(accountUpdated);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Delete account
        if(!this.accountRepository.existsById(id)){
			throw new ResourceNotFoundException("Account not found");
		}
        if(this.transactionService.existsByAccountId(id)){
            throw new NotValidException("Can't delete account with transactions");
        }
		this.accountRepository.deleteById(id);
	}

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getByClientId(Long clientId){
        List<Account> accounts = this.accountRepository.findByClientId(clientId);
        return this.accountMapper.toDtoList(accounts);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByClientId(Long clientId){
        return this.accountRepository.existsByClientId(clientId);
    }
    
}
