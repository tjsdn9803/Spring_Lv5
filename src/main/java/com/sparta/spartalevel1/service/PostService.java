package com.sparta.spartalevel1.service;

import com.sparta.spartalevel1.dto.PostRequestDto;
import com.sparta.spartalevel1.dto.PostResponseDto;
import com.sparta.spartalevel1.entity.Post;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
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

    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, User user) {
        Post post = findPost(id);
        if(post.getUser().getId() == user.getId()){
            post.update(postRequestDto, user);
        }else{
            throw new IllegalArgumentException("회원님이 작성하신 메모가 아닙니다.");
        }
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
}
