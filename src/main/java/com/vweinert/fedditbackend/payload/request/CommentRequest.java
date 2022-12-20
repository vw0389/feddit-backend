package com.vweinert.fedditbackend.payload.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CommentRequest {
    @NotBlank
    String content;
}
