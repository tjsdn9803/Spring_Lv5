package com.sparta.spartalevel1.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NO_PASSWORD(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),
    No_PID(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),

    //Login / Register
    LOGIN_NO(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
    NAME_SAME(HttpStatus.BAD_REQUEST, "중복된 username 입니다."),
    EMAIL_SAME(HttpStatus.BAD_REQUEST, "중복된 이메일 입니다."),
    WRONG_ADMIN_PASSWORD(HttpStatus.BAD_REQUEST, "관리자 암호가 다릅니다."),
    WRONG_NAME_WITHDRAWL(HttpStatus.BAD_REQUEST, "본인만 회원탈퇴할 수 있습니다."),


    //Token
    WRONG_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),

    //게시글, 댓글
    WRONG_POST_PID(HttpStatus.BAD_REQUEST, "잘못된 게시글 접근 입니다."),
    WRONG_COMMENT_PID(HttpStatus.BAD_REQUEST, "잘못된 댓글 접근 입니다."),
    WRONG_NAME(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),

    //좋아요
    WRONG_LIKE_PID(HttpStatus.BAD_REQUEST, "해당 좋아요가 존재하지 않습니다.")
    ;
    private final HttpStatus status;
    private final String message;

}
