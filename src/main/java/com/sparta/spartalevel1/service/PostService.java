package com.sparta.spartalevel1.service;

import com.sparta.spartalevel1.dto.CommentResponseDto;
import com.sparta.spartalevel1.dto.PostRequestDto;
import com.sparta.spartalevel1.dto.PostResponseDto;
import com.sparta.spartalevel1.entity.Comment;
import com.sparta.spartalevel1.entity.Post;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.exception.CustomException;
import com.sparta.spartalevel1.exception.ErrorCode;
import com.sparta.spartalevel1.repository.CommentRepository;
import com.sparta.spartalevel1.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        Post post = new Post(postRequestDto, user);
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
