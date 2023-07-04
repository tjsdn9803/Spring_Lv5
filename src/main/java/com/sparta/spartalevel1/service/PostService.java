package com.sparta.spartalevel1.service;

import com.sparta.spartalevel1.dto.CommentRequestDto;
import com.sparta.spartalevel1.dto.CommentResponseDto;
import com.sparta.spartalevel1.dto.PostRequestDto;
import com.sparta.spartalevel1.dto.PostResponseDto;
import com.sparta.spartalevel1.entity.Post;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.repository.CommentRepository;
import com.sparta.spartalevel1.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        PostResponseDto postResponseDto = new PostResponseDto(savePost);
        return postResponseDto;
    }


    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    @Transactional
    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, User user) {
        Post post = findPost(id);
        if(!user.getRole().getAuthority().equals("ROLE_ADMIN")){
            if(post.getUser().getId() != user.getId()){
                throw new IllegalArgumentException("회원님이 작성하신 메모가 아닙니다.");
            }
        }
        post.update(postRequestDto, user);
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다."));
    }

    public boolean deletePost(Long id, User user) {
        Post post = findPost(id);
        if(post.getUser().getId() == user.getId()){
            postRepository.delete(post);
            return true;
        }
        throw new IllegalArgumentException("회원님이 작성하신 메모가 아닙니다.");

    }

    public List<CommentResponseDto> findComments(Long id){
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        List<Comment> commentList= commentRepository.findAllByPostIdOrderByCreatedAtDesc(id);
        for(Comment a : commentList){
            CommentResponseDto commentResponseDto = new CommentResponseDto(a);
            System.out.println("commentResponseDto = " + commentResponseDto);
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }


}
