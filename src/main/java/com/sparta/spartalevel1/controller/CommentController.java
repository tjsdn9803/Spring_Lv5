package com.sparta.spartalevel1.controller;

import com.sparta.spartalevel1.dto.CommentRequestDto;
import com.sparta.spartalevel1.dto.CommentResponseDto;
import com.sparta.spartalevel1.dto.Result;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.entity.UserRoleEnum;
import com.sparta.spartalevel1.security.UserDetailsImpl;
import com.sparta.spartalevel1.service.CommentService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        System.out.println("user.getUsername() = " + user.getUsername());
        return commentService.createComment(commentRequestDto, user);
    }
    @GetMapping("/comment/search")
    public CommentResponseDto findComment(@RequestParam Long id){
        return commentService.findComment(id);
    }
    @GetMapping("/comments/search")
    public List<CommentResponseDto> findComments(@RequestBody CommentRequestDto commentRequestDto){
        System.out.println(commentRequestDto.getPostId());
        return commentService.findComments(commentRequestDto);
    }

    @PutMapping("/comment")
    public CommentResponseDto updateComment(@RequestParam Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return commentService.updateComment(id, commentRequestDto, user);
    }

    @DeleteMapping("/comment")
    public Result deleteComment(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        if(commentService.deleteComment(id, user)){
            Result result = new Result("댓글 삭제 성공", 200);
            return result;
        }else{
            Result result = new Result("삭제 불가능", 403);
            return result;
        }
    }
    @Secured(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/comment/secured")
    public CommentResponseDto updateCommentByAdmin(@RequestParam Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return commentService.updateComment(id, commentRequestDto, user);
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @DeleteMapping("/comment/secured")
    public Result deleteCommentByAdmin(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        if(commentService.deleteComment(id, user)){
            Result result = new Result("댓글 삭제 성공", 200);
            return result;
        }else{
            Result result = new Result("삭제 불가능", 403);
            return result;
        }
    }

}
