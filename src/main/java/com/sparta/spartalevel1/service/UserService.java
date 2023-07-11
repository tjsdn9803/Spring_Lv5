package com.sparta.spartalevel1.service;


import com.sparta.spartalevel1.dto.SignupRequestDto;
import com.sparta.spartalevel1.entity.User;
import com.sparta.spartalevel1.entity.UserRoleEnum;
import com.sparta.spartalevel1.exception.CustomException;
import com.sparta.spartalevel1.exception.ErrorCode;
import com.sparta.spartalevel1.repository.UserRepository;
import com.sparta.spartalevel1.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (!(checkUsername.isEmpty())) {
            throw new CustomException(ErrorCode.NAME_SAME);
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_SAME);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if(requestDto.isAdmin()){
            if(!ADMIN_TOKEN.equals(requestDto.getAdminToken())){
                throw new CustomException(ErrorCode.WRONG_ADMIN_PASSWORD);
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, email, role);
        userRepository.save(user);
    }

    public void withdrawal(Long id, User user) {
        User user1 = userRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.LOGIN_NO));
        if(!user1.getId().equals(user.getId())){
            throw new CustomException(ErrorCode.WRONG_NAME_WITHDRAWL);
        }
        userRepository.delete(user);
        
    }
}