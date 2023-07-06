package com.sparta.spartalevel1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private String content;
}
