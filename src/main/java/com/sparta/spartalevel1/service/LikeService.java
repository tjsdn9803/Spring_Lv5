package com.sparta.spartalevel1.service;


import com.sparta.spartalevel1.entity.*;
import com.sparta.spartalevel1.exception.CustomException;
import com.sparta.spartalevel1.exception.ErrorCode;
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
        Post post = findPost(postId);
        PostLike postLike = new PostLike(post, user);
        postLikeRepository.save(postLike);
    }

    public void deletePostLike(Long postLikeId, User user) {
        PostLike postLike = postLikeRepository.findById(postLikeId).orElseThrow(() ->
                new CustomException(ErrorCode.WRONG_LIKE_PID));
        if(!postLike.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.WRONG_NAME);
        }
        postLikeRepository.delete(postLike);
    }

    public void commentLike(Long commentId, User user) {
        Comment comment = findComment(commentId);
        CommentLike commentLike = new CommentLike(comment, user);
        commentLikeRepository.save(commentLike);
    }
    public void deleteCommentLike(Long commentLikeId, User user) {
        CommentLike commentLike = commentLikeRepository.findById(commentLikeId).orElseThrow(() ->
                new CustomException(ErrorCode.WRONG_LIKE_PID));
        if(!commentLike.getUser().getId().equals(user.getId())){
            throw new CustomException(ErrorCode.WRONG_NAME);
        }
        commentLikeRepository.delete(commentLike);
    }


    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(()->
                new CustomException(ErrorCode.WRONG_POST_PID)
        );
    }

    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()->
                new CustomException(ErrorCode.WRONG_POST_PID)
        );
    }

}




