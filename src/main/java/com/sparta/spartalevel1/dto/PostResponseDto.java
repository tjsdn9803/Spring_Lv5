package com.sparta.spartalevel1.dto;

import com.sparta.spartalevel1.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Long like;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String categoryId;
    private List<CommentResponseDto> commentList = new ArrayList<>();


    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.like = (long) post.getPostLikeList().size();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.categoryId = post.getCategory().getCategory_name();
        this.commentList = post.getCommentList().stream().map(CommentResponseDto::new).toList();
    }

    public PostResponseDto(Post post, List<CommentResponseDto> commentList) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.commentList = commentList;
        this.like = (long) post.getPostLikeList().size();
    }
}
