package com.sparta.spartalevel1.exception;

import com.sparta.spartalevel1.dto.JsonResponse;
import com.sparta.spartalevel1.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler({IllegalArgumentException.class})
//    public ResponseEntity<Result> handleException(IllegalArgumentException e){
//        Result result = new Result(e.getMessage(), HttpStatus.BAD_REQUEST.value());
//        return new ResponseEntity<>(
//                // HTTP body
//                result,
//                // HTTP status code
//                HttpStatus.BAD_REQUEST
//        );
//    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(CustomException e){
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(JsonResponse.error(e.getErrorCode().getMessage(), e.getErrorCode().getStatus()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> applicationHandler(MethodArgumentNotValidException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(JsonResponse.error("fail", "잘못된 데이터 형식입니다."));
    }
}
