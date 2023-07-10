package com.sparta.spartalevel1.controller;

import com.sparta.spartalevel1.dto.JsonResponse;
import com.sparta.spartalevel1.dto.Result;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.security.UserDetailsImpl;
import com.sparta.spartalevel1.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like/post")
    public JsonResponse<Result> postLike(@RequestParam Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        likeService.postLike(postId, user);
        Result result = new Result("게시글에 좋아요가 저장되었습니다.", 200);
        return JsonResponse.success(result);
    }
    @DeleteMapping("/like/post")
    public JsonResponse<Result> deletePostLike(@RequestParam Long postLikeId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        likeService.deletePostLike(postLikeId,user);
        Result result = new Result("게시글에 좋아요가 삭제되었습니다.", 200);
        return JsonResponse.success(result);
    }

    @PostMapping("/like/comment")
    public JsonResponse<Result> likeComment(@RequestParam Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        likeService.commentLike(commentId, user);
        Result result = new Result("댓글에 좋아요가 저장되었습니다", 200);
        return JsonResponse.success(result);
    }

    @DeleteMapping("/like/comment")
    public JsonResponse<Result> deleteLikeComment(@RequestParam Long commentLikeId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        likeService.deleteCommentLike(commentLikeId, user);
        Result result = new Result("댓글에 좋아요가 삭제되었습니다", 200);
        return JsonResponse.success(result);
    }
}
