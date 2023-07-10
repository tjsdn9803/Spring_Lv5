package com.sparta.spartalevel1.controller;


import com.sparta.spartalevel1.dto.JsonResponse;
import com.sparta.spartalevel1.dto.Result;
import com.sparta.spartalevel1.dto.SignupRequestDto;
import com.sparta.spartalevel1.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    public JsonResponse<Result> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult, HttpServletResponse httpServletResponse) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return JsonResponse.error("fail",new Result("회원가입에 실패하였습니다.", 403));
        }
        userService.signup(requestDto);
        Result result = new Result("회원가입을 성공하였습니다", 200);
        return JsonResponse.success(result);
    }

}