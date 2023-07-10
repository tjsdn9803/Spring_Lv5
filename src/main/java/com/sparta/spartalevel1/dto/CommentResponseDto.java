package com.sparta.spartalevel1.dto;

import com.sparta.spartalevel1.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String username;
    private Long like;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment saveComment) {
        this.id = saveComment.getId();
        this.content = saveComment.getContent();
        this.username = saveComment.getUser().getUsername();
        this.createdAt = saveComment.getCreatedAt();
        this.modifiedAt = saveComment.getModifiedAt();
        this.like = (long) saveComment.getCommentLikeList().size();
    }
}
