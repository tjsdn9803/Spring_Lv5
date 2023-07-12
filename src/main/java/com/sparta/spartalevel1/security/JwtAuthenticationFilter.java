package com.sparta.spartalevel1.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartalevel1.dto.LoginRequestDto;
import com.sparta.spartalevel1.dto.TokenDto;
import com.sparta.spartalevel1.entity.RefreshToken;
import com.sparta.spartalevel1.entity.UserRoleEnum;
import com.sparta.spartalevel1.exception.CustomException;
import com.sparta.spartalevel1.exception.ErrorCode;
import com.sparta.spartalevel1.jwt.JwtUtil;
import com.sparta.spartalevel1.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        TokenDto tokenDto = jwtUtil.createAllToken(username, role);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUsername(username);

        if(refreshToken.isPresent()){
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        }else{
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), username);
            refreshTokenRepository.save(newToken);
        }

        int status = response.getStatus();
        String a = "{\n \"msg\" : \"login success\", \n \"status\" : \""+ status+ "\" \n}";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValueAsString(a);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().print(a);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, tokenDto.getRefreshToken());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
        int status = response.getStatus();
        String a = "{\n \"msg\" : \"login failed\", \n \"status\" : \""+ status+ "\" \n}";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValueAsString(a);
            response.getWriter().print(a);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}