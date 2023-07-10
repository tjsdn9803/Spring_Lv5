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
    public PostResponseDto createPost(@RequestBody @Valid PostRequestDto postRequestDto, BindingResult bindingResult, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
        }
        User user = userDetails.getUser();
        return JsonResponse.success(postService.createPost(postRequestDto, user));
    }

    @GetMapping("/posts/search")
    public JsonResponse<List<PostResponseDto>> getPosts(){
        return JsonResponse.success(postService.getPosts());
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
