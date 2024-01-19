package com.mars.statement.global.jwt;

import com.mars.statement.domain.user.User;
import com.mars.statement.global.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // requset 에서 Authorization Header 값 찾음
        String authorization = request.getHeader("Authorization");

        System.out.println(authorization);

        // Authorization 헤더 검증
        if(authorization == null || !authorization.startsWith("Bearer ")){
            System.out.println("token null");
            filterChain.doFilter(request, response);
            // 조건이 해당되면 메소드 종료
            return;
        }

        // 토큰 접두사 제거
        String token = authorization.split(" ")[1];

        // 토큰 소멸시간 검증
        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            // 조건이 해당되면 메소드 종료
            return;
        }

        // 토큰에서 username, role 추출
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // User를 생성하여 값 set
        User user = new User();
        user.setUsername(username);
        user.setRole(role);
        user.setPassword("temppassword");

        // UserDetails에 객체 정보 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
