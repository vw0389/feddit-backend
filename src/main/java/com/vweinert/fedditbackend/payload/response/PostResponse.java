package com.vweinert.fedditbackend.payload.response;

import com.vweinert.fedditbackend.models.Comment;
import com.vweinert.fedditbackend.models.User;

import java.time.LocalDateTime;
import java.util.List;



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
    private List<Comment> comments;
    private User user;
    public PostResponse(Long id, String title, String content, LocalDateTime createdAt){
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
    public PostResponse(Long id, String title, String content, LocalDateTime createdAt,LocalDateTime modifiedAt,List<Comment> comments){
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.comments = comments;
    }
}
