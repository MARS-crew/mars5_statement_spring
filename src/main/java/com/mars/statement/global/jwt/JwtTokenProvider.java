package com.mars.statement.global.jwt;

import com.mars.statement.api.auth.domain.User;
import com.mars.statement.global.dto.TokenDto;
import com.mars.statement.global.exception.UnAuthenticationException;
import com.mars.statement.global.service.CustomUserDetailService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final CustomUserDetailService userDetailService;
    private static final String AUTHORIZATION_KEY = "Authorization";
    @Value("${jwt.accessTokenValidityInSeconds}")
    private String accessTokenValidationTime;
    @Value("${jwt.refreshTokenValidityInSeconds}")
    private String refreshTokenValidationTime;

    @Value("${jwt.secret}")
    private String secret;

    // token 발급
    public TokenDto issueToken(Long id) {
        final String accessToken = createToken(id, Long.valueOf(accessTokenValidationTime));
        final String refreshToken = createToken(id, Long.valueOf(refreshTokenValidationTime));

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // token 재발급
    public TokenDto reissueToken(Long id, String refreshToken) {
        final String accessToken = createToken(id, Long.valueOf(accessTokenValidationTime));

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // token 발급 로직
    public String createToken(Long id, Long expireLength){
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // 토큰에서 UserId 가져오기
    public String getUserId(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
    }
    // Spring Security 인증 과정 중 권한 확인 필요
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailService.loadUserByUsername(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에 있는 유저 정보 추출
    public String extract(HttpServletRequest request) throws Exception {

        return getAuthorizationToken(request);
    }

    // 헤더 체크
    public String getAuthorizationToken(HttpServletRequest request) throws Exception {
        String token = getRequestToken(request);

        if (token == null) {
            throw new UnAuthenticationException("JWT 토큰이 잘못되었습니다");
        }
        if (!StringUtils.hasText(token)) {
            throw new UnAuthenticationException("JWT 토큰이 잘못되었습니다");
        }
        if (!token.startsWith("Bearer ")) {
            throw new UnAuthenticationException("JWT 토큰이 잘못되었습니다");
        }

        final String jwt = token.substring(7);
        if (validateToken(jwt)) {
            throw new UnAuthenticationException("JWT 토큰이 잘못되었습니다");
        }
        return jwt;
    }

    // 헤더에서 토큰 획득
    public String getRequestToken(HttpServletRequest request) {
        if (request.getParameter(AUTHORIZATION_KEY)==null){
            return request.getHeader(AUTHORIZATION_KEY);
        }
        return request.getParameter(AUTHORIZATION_KEY);
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            // Bearer 검증
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            return false;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return false;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            return false;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            return false;
        }
    }
}