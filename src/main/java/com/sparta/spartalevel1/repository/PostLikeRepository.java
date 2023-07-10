package com.sparta.spartalevel1.repository;

import com.sparta.spartalevel1.entity.Post;
import com.sparta.spartalevel1.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    List<PostLike> findByPostId(Long id);
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

}
