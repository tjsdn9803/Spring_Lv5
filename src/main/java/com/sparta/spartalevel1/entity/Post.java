package com.sparta.spartalevel1.entity;

import com.sparta.spartalevel1.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
public class Post extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "author", nullable = false, length = 500)
    private String author;

    public Post(PostRequestDto postRequestDto){
        this.title = postRequestDto.getTitle();
        this.author = postRequestDto.getAuthor();
        this.password = postRequestDto.getPassword();
        this.content = postRequestDto.getContent();
    }

}
