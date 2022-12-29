package com.vweinert.fedditbackend.dto;

import com.vweinert.fedditbackend.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String about;
    private LocalDateTime createdAt;
    private String jwt;
    private Set<Role> roles;
}
