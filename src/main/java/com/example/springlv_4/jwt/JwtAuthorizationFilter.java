package com.example.springlv_4.jwt;

import com.example.springlv_4.dto.ApiResponseDto;
import com.example.springlv_4.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가") // 로그 찍는 용도
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    // Java Object -> JSON or JSON -> Java Object 일 경우에 사용
    private final ObjectMapper objectMapper;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    // OncePerRequestFilter 안에 있는 메서드, 추상 메서드로 되어있기 때문에 직접 구현해주어야 한다.
    // 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // header의 토큰을 가져오는 역할
        String token = jwtUtil.resolveToken(request);

        // 위에서 받아온 토큰이 정상적으로 존재한다면
        if(token != null) {
            // 토큰 검증
            if(!jwtUtil.validateToken(token)){
                // 메시지와 httpStatus를 담는 객체 생성
                ApiResponseDto responseDto = new ApiResponseDto("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value());
                // http 응답 코드 지정
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                // setHeader를 통해 지정해도 좋지만 좀 더 눈에 보이게 끔 지정 -> uft-8을 사용하고, json으로 줄 것임을 지정
                response.setContentType("application/json; charset=UTF-8");
                // writer 객체를 얻고 해당 객체를 통해 response body message를 생성하여 응답
                response.getWriter().write(objectMapper.writeValueAsString(responseDto));
                // 토큰이 검증되지 않았기 때문에 종료
                return;
            }
            // jwt의 payload 부분에 토큰에 담을 정보가 있는데 여기에 있는 정보 한 조각을 claim이라고 하며 name/value 쌍으로 존재한다.
            // 토큰에서 사용자 정보를 가져와 info에 할당
            Claims info = jwtUtil.getUserInfoFromToken(token);
            // 인증 처리 과정으로 보낸다. -> getSubject를 통해서 username을 받아온다.
            setAuthentication(info.getSubject());
        }

        // 메서드 종료시 다음 필터로 넘어가게 하는 역할
        filterChain.doFilter(request, response);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        // SecurityContextHolder 객체 안에 SecurityContext가 존재한다.
        // SecurityContextHolder는 ThreadLocal에 저장되기 때문에 각기 다른 쓰레드별로
        // 다른 SecurityContextHolder 인스턴스를 가지고 있어서 사용자 별로 각기 다른 인증 객체를 가질 수 있다.
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // 인증 객체를 생성
        // Authentication 클래스는 현재 접근하는 주체의 정보와 권한을 담는 인터페이스고 SecurityContext 저장되며
        // SecurityContextHolder를 통해 SecurityContext에 접근하고, SecurityContext를 통해 Authentication에 접근할 수 있다.
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        // username -> user 조회 -> userDetails 에 담고 -> authentication의 principal 에 담고
        // -> securityContent 에 담고 -> SecurityContextHolder 에 담고
        // -> 이제 @AuthenticationPrincipal 로 조회할 수 있음
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // Authentication 안에 principal, credentials, authorities가 있고
        // 인증 전에는 principal에 아이디, credentials에 비밀번호를 제공하기 위해 사용
        // 인증 후에는 principal에 인증이 완료된 userDetails의 구현체를 저장
        // credentials는 인증 완료후 유출 가능성을 줄이기 위해 삭제
        // authorities는 인증된 사용자가 가진 권한을 저장
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
