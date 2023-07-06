package com.sparta.spartalevel1.service;

import com.sparta.spartalevel1.dto.CommentRequestDto;
import com.sparta.spartalevel1.dto.CommentResponseDto;
import com.sparta.spartalevel1.entity.Comment;
import com.sparta.spartalevel1.entity.Post;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.repository.CommentRepository;
import com.sparta.spartalevel1.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepsitory;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepsitory, PostRepository postRepository) {
        this.commentRepsitory = commentRepsitory;
        this.postRepository = postRepository;
    }

    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, User user) {
        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow();
        System.out.println("user.getUsername() = " + user.getUsername());
        Comment comment = new Comment(commentRequestDto, user, post);
        System.out.println("user.getUsername() = " + user.getUsername());
        Comment saveComment = commentRepsitory.save(comment);
        System.out.println("saveComment.getUser().getUsername() = " + saveComment.getUser().getUsername());
        CommentResponseDto commentResponseDto = new CommentResponseDto(saveComment);
        return commentResponseDto;
    }
    public CommentResponseDto findComment(Long id){
        Comment comment = commentRepsitory.findById(id).orElseThrow();
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return commentResponseDto;
    }
    public List<CommentResponseDto> findComments(CommentRequestDto commentRequestDto){
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        List<Comment> commentList = commentRepsitory.findAllByPostIdOrderByCreatedAtDesc(commentRequestDto.getPostId());
        if(commentList.size() == 0){
            throw new IllegalArgumentException("선택한 게시글이 존재하지 않습니다.");
        }
        for(Comment a : commentList){
            CommentResponseDto commentResponseDto = new CommentResponseDto(a);
            System.out.println("commentResponseDto = " + commentResponseDto);
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }

    public CommentResponseDto getComment(Long id){
        Comment comment = findComment(id);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(long id, CommentRequestDto commentRequestDto, User user) {
        Comment comment = commentRepsitory.findById(id).orElseThrow();
        if(!user.getRole().getAuthority().equals("ROLE_ADMIN")){
            if(comment.getUser().getId() != user.getId()){
                throw new IllegalArgumentException("회원님이 작성하신 댓글이 아닙니다.");
            }
        }
        comment.update(commentRequestDto, user);
        return new CommentResponseDto(comment);
    }


    public void deleteComment(Long id, User user) {
        Comment comment = findComment(id);
        if(!user.getRole().getAuthority().equals("ROLE_ADMIN")){
            if(comment.getUser().getId() != user.getId()){
                throw new IllegalArgumentException("회원님이 작성하신 댓글이 아닙니다.");
            }
        }
        commentRepsitory.delete(comment);
    }

    public Comment findComment(Long id){
        return commentRepsitory.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글은 존재하지 않습니다"));
    }
}
