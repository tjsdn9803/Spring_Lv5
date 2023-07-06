package com.sparta.spartalevel1.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartalevel1.dto.Result;
import com.sparta.spartalevel1.dto.SignupRequestDto;
import com.sparta.spartalevel1.security.UserDetailsImpl;
import com.sparta.spartalevel1.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/user/signup")
    public ResponseEntity signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult, HttpServletResponse httpServletResponse) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return new ResponseEntity(new Result("형식에 맞지 않습니다.", 403), HttpStatusCode.valueOf(403));
        }
        // 중복 username, email 예외처리
        try{
            userService.signup(requestDto);
        }catch (Exception e){
            return new ResponseEntity(new Result(e.getMessage(), 403), HttpStatusCode.valueOf(403));
        }
        return new ResponseEntity(new Result("회원가입 성공", 200),HttpStatusCode.valueOf(200));
    }

}