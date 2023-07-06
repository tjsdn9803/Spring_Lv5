package com.sparta.spartalevel1.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title;
    @Size(max = 500, message = "500자를 넘을 수 없습니다.")
    private String content;
}
