package com.sparta.spartalevel1.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category_name;

    @OneToMany(mappedBy = "category")
    private List<Post> postList = new ArrayList<>();





}
