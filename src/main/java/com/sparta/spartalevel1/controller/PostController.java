package com.sparta.spartalevel1.controller;

import com.sparta.spartalevel1.dto.PostRequestDto;
import com.sparta.spartalevel1.dto.PostResponseDto;
import com.sparta.spartalevel1.dto.Result;
import com.sparta.spartalevel1.entity.Post;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.entity.UserRoleEnum;
import com.sparta.spartalevel1.security.UserDetailsImpl;
import com.sparta.spartalevel1.service.PostService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return postService.createPost(postRequestDto, user);
    }

    @GetMapping("/post/search")
    public List<PostResponseDto> getPosts(){
        return postService.getPosts();
    }

    @GetMapping("/post/search/{id}")
    public PostResponseDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    @PutMapping("/post")
    public PostResponseDto updatePost(@RequestParam Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return postService.updatePost(id, postRequestDto, user);
    }

    @DeleteMapping("/post")
    public Result deletePost(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        if(postService.deletePost(id, user)){
            Result result = new Result("삭제", 200);
            return result;
        }else{
            Result result = new Result("삭제 불가능", 403);
            return result;
        }
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/post/secured")
    public PostResponseDto updatePostByAdmin(@RequestParam Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return postService.updatePost(id, postRequestDto, user);
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @DeleteMapping("/post/secured")
    public Result deletePostByAdmin(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        if(postService.deletePost(id, user)){
            Result result = new Result("삭제", 200);
            return result;
        }else{
            Result result = new Result("삭제 불가능", 403);
            return result;
        }
    }

}
