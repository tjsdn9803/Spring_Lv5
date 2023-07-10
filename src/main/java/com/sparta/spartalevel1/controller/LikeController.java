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
    }
    @DeleteMapping("/like/post")
    public void deletePostLike(@RequestParam Long postLikeId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        likeService.deletePostLike(postLikeId,user);
    }

//    @PostMapping("/like/comment")
//    public likeResponseDto likePost(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//    }


}