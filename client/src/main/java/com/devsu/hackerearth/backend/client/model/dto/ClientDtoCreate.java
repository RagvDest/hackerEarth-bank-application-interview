package com.devsu.hackerearth.backend.client.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.AssertTrue;

@Data
@AllArgsConstructor
public class ClientDtoCreate {

	@NotEmpty(message = "dNI is required")
	private String dni;
	@NotEmpty(message = "name is required")
	private String name;
	@NotEmpty(message = "password is required")
	private String password;
	@NotEmpty(message = "gender is required")
	private String gender;
	@NotNull(message = "age is required")
	private int age;
	@NotEmpty(message = "address is required")
	private String address;
	@NotEmpty(message = "phone is required")
	private String phone;
	@AssertTrue(message = "active must be true")
	private boolean isActive;
}
