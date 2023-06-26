package com.sparta.spartalevel1;

import com.sparta.spartalevel1.dto.PostRequestDto;
import com.sparta.spartalevel1.entity.Post;
import com.sparta.spartalevel1.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class PostTest {

    @Autowired
    PostRepository postRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("포스트 생성하기")
    void test1() {
        Post post = new Post();
        post.setTitle("title1");
        post.setId(1L);
        post.setContent("content1");
        post.setPassword("password1");
        post.setAuthor("author1");

        postRepository.save(post);
    }

}
