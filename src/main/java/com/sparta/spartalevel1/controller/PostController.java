package com.sparta.spartalevel1.controller;

import com.sparta.spartalevel1.dto.PostRequestDto;
import com.sparta.spartalevel1.dto.PostResponseDto;
import com.sparta.spartalevel1.dto.Result;
import com.sparta.spartalevel1.entity.Post;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.entity.UserRoleEnum;
import com.sparta.spartalevel1.security.UserDetailsImpl;
import com.sparta.spartalevel1.service.PostService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/posts/search")
    public List<PostResponseDto> getPosts(){
        return postService.getPosts();
    }

    @GetMapping("/post/search")
    public PostResponseDto getPost(@RequestParam Long id){
        return postService.getPost(id);
    }

    @PutMapping("/post")
    public PostResponseDto updatePost(@RequestParam Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return postService.updatePost(id, postRequestDto, user);
    }

    @DeleteMapping("/post")
    public ResponseEntity deletePost(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        try{
            postService.deletePost(id,user);
        }catch (Exception e){
            return new ResponseEntity(new Result(e.getMessage(), 400), HttpStatusCode.valueOf(400));
        }
        return new ResponseEntity(new Result("삭제에 성공하였습니다", 200), HttpStatusCode.valueOf(200));
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/post/secured")
    public PostResponseDto updatePostByAdmin(@RequestParam Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return postService.updatePost(id, postRequestDto, user);
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @DeleteMapping("/post/secured")
    public ResponseEntity deletePostByAdmin(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        try {
            postService.deletePost(id, user);
        }catch (Exception e){
            return new ResponseEntity(new Result(e.getMessage(), 403), HttpStatusCode.valueOf(403));
        }
        return new ResponseEntity(new Result("삭제 성공", 200), HttpStatusCode.valueOf(200));
    }

}
