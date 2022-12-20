package com.vweinert.fedditbackend.entities;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false,unique = true)
    private String email;
    @Column(nullable=false,updatable = false,unique = true)
    private String username;
    @Column(nullable=false)
    private String password;
    @Column(columnDefinition = "text")
    private String about;
    @Column(nullable=false,updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime passwordChangedAt;
    private LocalDateTime aboutChangedAt;
    @Column(nullable = false)
    private Boolean deleted = false;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @JsonManagedReference
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private Set<Post> posts;
    @JsonManagedReference
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private Set<Comment> comments;

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
