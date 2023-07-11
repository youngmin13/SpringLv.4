package com.example.springlv_4.jwt;

import com.example.springlv_4.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 키값. 사용자 권한도 토큰안에 넣어주기 때문에 그때 사용하는 키값
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자, Bearer type
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정 -> 굳이 볼게 아니여서 이번 과제에서는 필요 없을 듯 하다.
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header 토큰을 가져오기 Keys.hmacShaKeyFor(bytes);
    public String resolveToken (HttpServletRequest request) {
        // Http프로토콜의 request중 header에 해당하는 값을 가져옴 -> header의 이름이 AUTHORIZATION_HEADER 인 것
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        // 해당하는 토큰이 Http프로토콜의 request 헤더 값을 가지고 있으면서
        // 해당 토큰이 "Bearer "로 시작한다면
        // -> 해당 토큰의 앞 7글자 (Bearer )을 자르고 리턴
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            // "Bearer " 이 7글자라서 자르고 리턴
            return bearerToken.substring(7);
        }
        // 아니면 해당하는 토큰이 존재하지 않는 것
        return null;
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        // 토큰 만료시간
        // 60분
        long TOKEN_TIME = 60 * 60 * 1000L;
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID) -> username으로 지정
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            // 토큰의 위변조, 만료 체크
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    // Claims는 JWT의 body이고 JWT 생성자가 JWT를 받는이들에게 제시하기 바라는 정보를 포함
    public Claims getUserInfoFromToken(String token) {
        // Jwts.parserBuilder() 를 통해서 JWS 서명을 확인하는 데 사용할 SecretKey 또는 비대칭 공개키를 지정
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
