package com.sparta.spartalevel1.controller;

import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.security.UserDetailsImpl;
import com.sparta.spartalevel1.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like/post")
    public void postLike(@RequestParam Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        likeService.postLike(postId, user);
        return new ResponseEntity(new Result("게시글에 좋아요가 저장되었습니다.", 200), HttpStatusCode.valueOf(200));
    }
    @DeleteMapping("/like/post")
    public ResponseEntity deletePostLike(@RequestParam Long postLikeId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        likeService.deletePostLike(postLikeId,user);
        return new ResponseEntity(new Result("게시글에 좋아요가 삭제되었습니다.", 200), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/like/comment")
    public ResponseEntity likeComment(@RequestParam Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        likeService.commentLike(commentId, user);
        return new ResponseEntity(new Result("댓글에 좋아요가 저장되었습니다.", 200), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/like/comment")
    public ResponseEntity deleteLikeComment(@RequestParam Long commentLikeId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        likeService.deleteCommentLike(commentLikeId, user);
        return new ResponseEntity(new Result("댓글에 좋아요가 삭제되었습니다", 200), HttpStatusCode.valueOf(200));
    }


}
