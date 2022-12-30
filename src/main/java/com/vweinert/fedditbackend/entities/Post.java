package com.vweinert.fedditbackend.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="posts", indexes = {@Index(name="on_created_at", columnList = "createdAt DESC")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable=false,updatable = false)
    private String title;
    @Column(nullable=false,columnDefinition = "text")
    private String content;
    @Column(nullable=false,updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
    @Column(nullable=false)
    private Boolean deleted = false;
}
