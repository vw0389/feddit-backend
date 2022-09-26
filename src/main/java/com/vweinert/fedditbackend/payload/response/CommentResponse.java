package com.vweinert.fedditbackend.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Data
public class CommentResponse {
    private Long userId;
    private Long postId;
    private Long id;
    private String content;
}
