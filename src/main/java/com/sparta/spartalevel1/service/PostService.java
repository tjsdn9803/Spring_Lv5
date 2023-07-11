package com.sparta.spartalevel1.service;

import com.sparta.spartalevel1.dto.CommentResponseDto;
import com.sparta.spartalevel1.dto.PostRequestDto;
import com.sparta.spartalevel1.dto.PostResponseDto;
import com.sparta.spartalevel1.entity.Category;
import com.sparta.spartalevel1.entity.Comment;
import com.sparta.spartalevel1.entity.Post;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.exception.CustomException;
import com.sparta.spartalevel1.exception.ErrorCode;
import com.sparta.spartalevel1.repository.CategoryRepository;
import com.sparta.spartalevel1.repository.CommentRepository;
import com.sparta.spartalevel1.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.categoryRepository = categoryRepository;
    }

    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        Category category = categoryRepository.findById(postRequestDto.getCategoryId()).orElseThrow(() ->
                new CustomException(ErrorCode.WRONG_CATEGORY_ID));
        Post post = new Post(postRequestDto, user,category);
        Post savePost = postRepository.save(post);
        return new PostResponseDto(savePost);
    }


    public List<PostResponseDto> getPosts() {
        List<Post> list = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto> responseDtoList = new ArrayList<>();
        for(Post a : list){
            List<CommentResponseDto> commentResponseDtoList = findComments(a.getId());
            PostResponseDto postResponseDto = new PostResponseDto(a, commentResponseDtoList);
            responseDtoList.add(postResponseDto);
        }
        return responseDtoList;
    }

    public Page<PostResponseDto> getPosts(int size, int page, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> productList = postRepository.findAll(pageable);
        return productList.map(PostResponseDto::new);
    }

    public Page<PostResponseDto> getPostsByCategory(int size, int page, String sortBy, boolean isAsc, Long categoryId){
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> productList = postRepository.findAllByCategoryId(categoryId, pageable);
        return productList.map(PostResponseDto::new);
    }

    @Transactional
    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        List<CommentResponseDto> commentResponseDtoList = findComments(id);
        return new PostResponseDto(post, commentResponseDtoList);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, User user) {
        Post post = findPost(id);
        if(!user.getRole().getAuthority().equals("ROLE_ADMIN")){
            if(post.getUser().getId() != user.getId()){
                throw new CustomException(ErrorCode.WRONG_NAME);
            }
        }
        post.update(postRequestDto, user);
        List<CommentResponseDto> commentResponseDtoList = findComments(id);
        return new PostResponseDto(post, commentResponseDtoList);
    }

    public void deletePost(Long id, User user) {
        Post post = findPost(id);
        if(!user.getRole().getAuthority().equals("ROLE_ADMIN")){
             if(post.getUser().getId() != user.getId()){
                 throw new CustomException(ErrorCode.WRONG_NAME);
             }
        }
        postRepository.delete(post);
    }

    public List<CommentResponseDto> findComments(Long id){
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        List<Comment> commentList= commentRepository.findAllByPostIdOrderByCreatedAtDesc(id);
        for(Comment a : commentList){
            CommentResponseDto commentResponseDto = new CommentResponseDto(a);
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(()->
                new CustomException(ErrorCode.WRONG_POST_PID));
    }

}
