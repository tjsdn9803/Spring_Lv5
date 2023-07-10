package com.sparta.spartalevel1.controller;

import com.sparta.spartalevel1.dto.CommentRequestDto;
import com.sparta.spartalevel1.dto.CommentResponseDto;
import com.sparta.spartalevel1.dto.JsonResponse;
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
    public JsonResponse<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return JsonResponse.success(commentService.createComment(commentRequestDto, user));
    }

    @GetMapping("/comment/search")
    public JsonResponse<CommentResponseDto> findComment(@RequestParam Long id){
        return JsonResponse.success(commentService.getComment(id));
    }
    @GetMapping("/comments/search")
    public JsonResponse<List<CommentResponseDto>> findComments(@RequestBody CommentRequestDto commentRequestDto){
        return JsonResponse.success(commentService.getComments(commentRequestDto));
    }

    @PutMapping("/comment")
    public JsonResponse<CommentResponseDto> updateComment(@RequestParam Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return JsonResponse.success(commentService.updateComment(id, commentRequestDto, user));
    }

    @DeleteMapping("/comment")
    public JsonResponse<Result> deleteComment(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        commentService.deleteComment(id, user);
        Result result = new Result("삭제 성공", 200);
        return JsonResponse.success(result);
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/comment/secured")
    public JsonResponse<CommentResponseDto> updateCommentByAdmin(@RequestParam Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return JsonResponse.success(commentService.updateComment(id, commentRequestDto, user));
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @DeleteMapping("/comment/secured")
    public JsonResponse<Result> deleteCommentByAdmin(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        commentService.deleteComment(id, user);
        Result result = new Result("삭제 성공", 200);
        return JsonResponse.success(result);
    }

}
