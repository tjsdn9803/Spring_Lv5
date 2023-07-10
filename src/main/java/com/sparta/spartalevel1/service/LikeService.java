package com.sparta.spartalevel1.service;

import com.sparta.spartalevel1.entity.Post;
import com.sparta.spartalevel1.entity.PostLike;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.repository.CommentLikeRepository;
import com.sparta.spartalevel1.repository.CommentRepository;
import com.sparta.spartalevel1.repository.PostLikeRepository;
import com.sparta.spartalevel1.repository.PostRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void postLike(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(()->
                new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        PostLike postLike = new PostLike(post, user);
        postLikeRepository.save(postLike);
    }

}
