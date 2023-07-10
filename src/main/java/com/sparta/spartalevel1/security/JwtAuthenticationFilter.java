package com.sparta.spartalevel1.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartalevel1.dto.LoginRequestDto;
import com.sparta.spartalevel1.entity.UserRoleEnum;
import com.sparta.spartalevel1.jwt.JwtUtil;
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

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
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

        String token = jwtUtil.createToken(username, role);
        int status = response.getStatus();
        String a = "{ \"msg\" : \"login success\", \n \"status\" : \""+ status+ "\" \n}";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValueAsString(a);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().print(a);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
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