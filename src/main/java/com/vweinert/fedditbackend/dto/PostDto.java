package com.vweinert.fedditbackend.dto;
import java.time.LocalDateTime;

import com.vweinert.fedditbackend.entities.User;
import com.vweinert.fedditbackend.entities.Comment;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Comment> comments;
    private User user;
}
