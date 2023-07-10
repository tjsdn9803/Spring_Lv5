package com.sparta.spartalevel1.repository;

import com.sparta.spartalevel1.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
