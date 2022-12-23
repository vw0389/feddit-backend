package com.vweinert.fedditbackend.payload.request;

import javax.validation.constraints.NotBlank;

import lombok.NoArgsConstructor;
import lombok.Data;
@Data
@NoArgsConstructor
public class LoginRequest {
	@NotBlank(message = "missing username")
  	private String username;

	@NotBlank(message = "missing password")
	private String password;

}
