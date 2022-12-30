package com.vweinert.fedditbackend.entities;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.ManyToOne;

import javax.persistence.Table;

import com.vweinert.fedditbackend.request.comment.PostComment;
import com.vweinert.fedditbackend.request.comment.PutComment;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull(groups = {PutComment.class})
    private Long id;
    @Column(nullable=false,columnDefinition = "text")
    @NotEmpty(groups = {PutComment.class, PostComment.class})
    private String content;
    @Column(nullable=false,updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="post_id", referencedColumnName = "id")
    private Post post;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;
    @Column(nullable=false)
    @Builder.Default
    private Boolean deleted = false;
}
