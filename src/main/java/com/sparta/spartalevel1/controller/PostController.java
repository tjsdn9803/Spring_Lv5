package com.sparta.spartalevel1.controller;

import com.sparta.spartalevel1.dto.PostRequestDto;
import com.sparta.spartalevel1.dto.PostResponseDto;
import com.sparta.spartalevel1.dto.Result;
import com.sparta.spartalevel1.entity.Post;
import com.sparta.spartalevel1.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    @PutMapping("/post")
    public PostResponseDto updatePost(@RequestParam Long id, @RequestBody PostRequestDto postRequestDto){
        return postService.updatePost(id, postRequestDto);
    }

    @DeleteMapping("/post")
    public Result deletePost(@RequestParam Long id, @RequestBody PostRequestDto postRequestDto) {
        Result result = new Result();
        result.setResult(postService.deletePost(id, postRequestDto));
        return result;
    }
}
