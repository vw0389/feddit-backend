package com.vweinert.fedditbackend.payload.request;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostRequest {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
}
