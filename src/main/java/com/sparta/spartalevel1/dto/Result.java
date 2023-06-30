package com.sparta.spartalevel1.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Result {
    private String msg;
    private int statusCode;
    public Result(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
