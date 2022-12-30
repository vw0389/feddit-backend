package com.vweinert.fedditbackend.dto;

import com.vweinert.fedditbackend.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private User user;
    private Boolean deleted;
}
