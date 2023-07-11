package com.sparta.spartalevel1.controller;

import com.sparta.spartalevel1.dto.JsonResponse;
import com.sparta.spartalevel1.dto.PostRequestDto;
import com.sparta.spartalevel1.dto.PostResponseDto;
import com.sparta.spartalevel1.dto.Result;
import com.sparta.spartalevel1.entity.Post;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.entity.UserRoleEnum;
import com.sparta.spartalevel1.security.UserDetailsImpl;
import com.sparta.spartalevel1.service.PostService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/post")
    public JsonResponse<PostResponseDto> createPost(@RequestBody @Valid PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return JsonResponse.success(postService.createPost(postRequestDto, user));
    }

    @GetMapping("/posts/search")
    public JsonResponse<List<PostResponseDto>> getPosts(){
        return JsonResponse.success(postService.getPosts());
    }

    @GetMapping("/posts/search/paging")
    public JsonResponse<Page<PostResponseDto>> getPostsPaging(int size, int page, String sortBy, boolean isAsc){
        System.out.println(1);
        return JsonResponse.success(postService.getPosts(size, page-1, sortBy, isAsc));
    }

    @GetMapping("/post/search")
    public JsonResponse<PostResponseDto> getPost(@RequestParam Long id){
        return JsonResponse.success(postService.getPost(id));
    }

    @PutMapping("/post")
    public JsonResponse<PostResponseDto> updatePost(@RequestParam Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return JsonResponse.success(postService.updatePost(id, postRequestDto, user));
    }

    @DeleteMapping("/post")
    public JsonResponse<Result> deletePost(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        postService.deletePost(id,user);
        Result result = new Result("삭제 성공", 200);
        return JsonResponse.success(result);
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/post/secured")
    public JsonResponse<PostResponseDto> updatePostByAdmin(@RequestParam Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return JsonResponse.success(postService.updatePost(id, postRequestDto, user));
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @DeleteMapping("/post/secured")
    public JsonResponse<Result> deletePostByAdmin(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        postService.deletePost(id, user);
        Result result = new Result("삭제 성공", 200);
        return JsonResponse.success(result);
    }

}
