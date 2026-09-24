package com.devsu.hackerearth.backend.account.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

	private Long id;
    private Date date;
	@NotEmpty(message = "type is required")
	private String type;
	@NotNull(message = "amount is required")
	private double amount;
	private double balance;
	@NotNull(message = "accountId is required")
	private Long accountId;
}
