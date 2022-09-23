package com.vweinert.fedditbackend.payload.response;

import com.vweinert.fedditbackend.models.Comment;

import java.time.LocalDateTime;
import java.util.Set;



import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostResponse {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Set<Comment> comments;

    public PostResponse(Long id, String title, String content, LocalDateTime createdAt){
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
