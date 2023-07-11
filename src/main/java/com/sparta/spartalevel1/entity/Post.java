package com.sparta.spartalevel1.entity;

import com.sparta.spartalevel1.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor
public class Post extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "author", nullable = false)
    private String author;

    private Long likeCount;

    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE)
    private List<PostLike> postLikeList = new ArrayList<>();

    public Post(PostRequestDto postRequestDto, User user, Category category){
        this.title = postRequestDto.getTitle();
        this.author = user.getUsername();
        this.user = user;
        this.content = postRequestDto.getContent();
        this.likeCount = 0L;
        this.category = category;
        category.getPostList().add(this);
    }

    public void update(PostRequestDto postRequestDto, User user) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.author = user.getUsername();
    }
    public void updateLikeCount(){
        this.likeCount +=1;
    }
}
