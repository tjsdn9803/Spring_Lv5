package com.sparta.spartalevel1.security;

import com.sparta.spartalevel1.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

//        String tokenValue = jwtUtil.getJwtFromHeader(req);
        String accessToken = jwtUtil.getJwtFromHeader(req, "Access");
        String refreshToken = jwtUtil.getJwtFromHeader(req, "Refresh");
//        String loginId = jwtUtil.getUsernameFromToken(refreshToken);



        if(accessToken!= null){
            if(jwtUtil.validateToken(accessToken)){
                setAuthentication(jwtUtil.getUsernameFromToken(accessToken));
            }else if(refreshToken != null){
                boolean isRefreshToken = jwtUtil.refreshTokenValidation(refreshToken);
                if(isRefreshToken){
                    String loginId = jwtUtil.getUsernameFromToken(refreshToken);
                    User user =  userRepository.findByUsername(loginId).orElseThrow(() -> new CustomException(ErrorCode.WRONG_NAME));
                    String newAccessToken = jwtUtil.createToken(loginId, user.getRole() , "Access");
                    System.out.println("newAccessToken = " + newAccessToken);
                    res.setHeader("Access_Token", newAccessToken);
                    Claims info = jwtUtil.getUserInfoFromToken(newAccessToken.substring(7));
                    System.out.println("info.getSubject() = " + info.getSubject());
                    setAuthentication(info.getSubject());
                } else{
                    System.out.println(1);
                    throw new CustomException(ErrorCode.WRONG_TOKEN);
                }
            }
        }

//        if (StringUtils.hasText(tokenValue)) {
//
//            if (!jwtUtil.refreshTokenValidation(tokenValue)) {
//                log.error("Token Error");
//                return;
//            }
//
//            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
//
//            try {
//                setAuthentication(info.getSubject());
//            } catch (Exception e) {
//                log.error(e.getMessage());
//                return;
//            }
//        }
        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}