package com.sparta.spartalevel1.controller;

import com.sparta.spartalevel1.dto.PostRequestDto;
import com.sparta.spartalevel1.dto.PostResponseDto;
import com.sparta.spartalevel1.service.PostService;
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
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
    }

    @GetMapping("/post")
    public List<PostResponseDto> getPosts(){
        return postService.getPosts();
    }

}
