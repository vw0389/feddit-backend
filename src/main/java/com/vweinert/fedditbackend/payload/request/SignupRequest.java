package com.vweinert.fedditbackend.payload.request;

import java.util.Set;

import javax.validation.constraints.*;

import lombok.Data;
@Data
public class SignupRequest {
  @NotBlank
  @Size(min = 8, max = 32)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private Set<String> role;

  @NotBlank
  @Size(min = 8, max = 32)
  private String password;
}
