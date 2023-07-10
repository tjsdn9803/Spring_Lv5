package com.sparta.spartalevel1.service;

import com.sparta.spartalevel1.dto.CommentRequestDto;
import com.sparta.spartalevel1.dto.CommentResponseDto;
import com.sparta.spartalevel1.entity.Comment;
import com.sparta.spartalevel1.entity.Post;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.exception.CustomException;
import com.sparta.spartalevel1.exception.ErrorCode;
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
        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(()->
            new CustomException(ErrorCode.WRONG_POST_PID)
        );
        Comment comment = new Comment(commentRequestDto, user, post);
        Comment saveComment = commentRepsitory.save(comment);
        return new CommentResponseDto(saveComment);
    }
    public List<CommentResponseDto> getComments(CommentRequestDto commentRequestDto){
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(()->
                new CustomException(ErrorCode.WRONG_POST_PID)
        );
        List<Comment> commentList = commentRepsitory.findAllByPostIdOrderByCreatedAtDesc(post.getId());
//        if(commentList.size() == 0){
//            throw new CustomException(ErrorCode.WRONG_COMMENT_PID);
//        }
        for(Comment a : commentList){
            CommentResponseDto commentResponseDto = new CommentResponseDto(a);
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }

    public CommentResponseDto getComment(Long id){
        Comment comment = findComment(id);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findComment(id);
        if(!user.getRole().getAuthority().equals("ROLE_ADMIN")){
            if(comment.getUser().getId() != user.getId()){
                throw new CustomException(ErrorCode.WRONG_NAME);
            }
        }
        comment.update(commentRequestDto, user);
        return new CommentResponseDto(comment);
    }


    public void deleteComment(Long id, User user) {
        Comment comment = findComment(id);
        if(!user.getRole().getAuthority().equals("ROLE_ADMIN")){
            if(comment.getUser().getId() != user.getId()){
                throw new CustomException(ErrorCode.WRONG_NAME);
            }
        }
        commentRepsitory.delete(comment);
    }

    public Comment findComment(Long id){
        return commentRepsitory.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.WRONG_COMMENT_PID));
    }
}
