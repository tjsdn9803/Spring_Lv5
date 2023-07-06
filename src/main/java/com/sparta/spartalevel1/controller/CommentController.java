package com.sparta.spartalevel1.controller;

import com.sparta.spartalevel1.dto.CommentRequestDto;
import com.sparta.spartalevel1.dto.CommentResponseDto;
import com.sparta.spartalevel1.dto.Result;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.entity.UserRoleEnum;
import com.sparta.spartalevel1.security.UserDetailsImpl;
import com.sparta.spartalevel1.service.CommentService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
        return commentService.createComment(commentRequestDto, user);
    }

    @GetMapping("/comment/search")
    public CommentResponseDto findComment(@RequestParam Long id){
        return commentService.getComment(id);
    }
    @GetMapping("/comments/search")
    public List<CommentResponseDto> findComments(@RequestBody CommentRequestDto commentRequestDto){
        return commentService.getComments(commentRequestDto);
    }

    @PutMapping("/comment")
    public CommentResponseDto updateComment(@RequestParam Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return commentService.updateComment(id, commentRequestDto, user);
    }

    @DeleteMapping("/comment")
    public ResponseEntity deleteComment(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        try {
            commentService.deleteComment(id, user);
        }catch (Exception e){
            return new ResponseEntity(new Result(e.getMessage(), 400), HttpStatusCode.valueOf(400));
        }
        return new ResponseEntity(new Result("삭제에 성공하였습니다.", 200), HttpStatusCode.valueOf(200));
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/comment/secured")
    public CommentResponseDto updateCommentByAdmin(@RequestParam Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return commentService.updateComment(id, commentRequestDto, user);
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @DeleteMapping("/comment/secured")
    public ResponseEntity deleteCommentByAdmin(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        try {
            commentService.deleteComment(id, user);
        }catch (Exception e){
            return new ResponseEntity(new Result(e.getMessage(), 403), HttpStatusCode.valueOf(403));
        }
        return new ResponseEntity(new Result("삭제 성공", 200), HttpStatusCode.valueOf(200));
    }

}
