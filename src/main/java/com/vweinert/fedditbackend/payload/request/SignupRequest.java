package com.vweinert.fedditbackend.payload.request;

import java.util.Set;

import javax.validation.constraints.*;

import lombok.Data;
@Data
public class SignupRequest {
  @NotBlank(message = "username is blank")
  @Size(min = 8, max = 32, message = "username must be between 8 and 32 characters")
  private String username;

  @NotBlank(message = "email is blank")
  @Email(message = "invalid email")
  private String email;

  private Set<String> role;

  @NotBlank(message = "Missing password")
  @Size(min = 8, max = 32, message = "password must be between 8 and 32 characters")
  private String password;
}
