package com.vweinert.fedditbackend.entities;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vweinert.fedditbackend.payload.auth.request.LoginRequest;
import com.vweinert.fedditbackend.payload.auth.request.SignupRequest;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false,unique = true)
    @NotBlank(groups = {SignupRequest.class}, message = "email is blank")
    @Email(groups = {SignupRequest.class}, message = "invalid email")
    private String email;
    @Column(nullable=false,updatable = false,unique = true)
    @NotBlank(groups = {LoginRequest.class, SignupRequest.class},message = "username is blank")
    @Size(min = 8, max = 32, groups = {LoginRequest.class, SignupRequest.class}, message = "username must be between 8 and 32 characters")
    private String username;
    @Column(nullable=false)
    @NotBlank(groups = {LoginRequest.class, SignupRequest.class},message = "Missing password")
    @Size(min = 8, max = 32, groups = {LoginRequest.class, SignupRequest.class}, message = "password must be between 8 and 32 characters")
    private String password;
    @Column(columnDefinition = "text")
    private String about;
    @Column(nullable=false,updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime passwordChangedAt;
    private LocalDateTime aboutChangedAt;
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
  
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private Set<Post> posts;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private Set<Comment> comments;
    @Transient
    @JsonInclude()
    private String jwt;
    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
