package com.devsu.hackerearth.backend.account.model.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

	private Long id;
	@NotEmpty(message = "number is required")
	private String number;
	@NotEmpty(message = "type is required")
	private String type;
	@NotNull(message = "initialAmount is required")
	@PositiveOrZero(message = "initialAmount can't be negative")
	private double initialAmount;
	@NotNull(message = "active is required")
	private boolean isActive;
	@NotNull(message = "clientId is required")
	private Long clientId;
}
