package com.vweinert.fedditbackend.payload.request;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostRequest {
    @NotEmpty(message = "missing title")
    private String title;
    @NotEmpty(message = "missing contents")
    private String content;
}
