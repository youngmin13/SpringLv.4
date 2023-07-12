package com.example.springlv_4.config;

import com.example.springlv_4.jwt.JwtAuthorizationFilter;
import com.example.springlv_4.jwt.JwtUtil;
import com.example.springlv_4.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

/**
 * 1. 인증설정
 * WebSecurityConfig > jwtUtil > UsernamePasswordAuthenticationFilter > SecurityFilterChain > 요청별 인증수행
 */
@Configuration // 빈 수동 등록을 위해서 사용
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;

    // 비밀번호를 BCryptPasswordEncoder 메서드로 인코딩해주는 메서드
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService, objectMapper);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        // Rest API를 이용한 서버라면, session 기반 인증과는 다르게 stateless하기 때문에 서버에 인증 정보를 보관 x
        // jwt 같은 토큰을 사용하여 인증하기 때문에 해당 토큰을 Cookie에 저장하지 않는다면 csrf 취약점에 대해서는 어느 정도 안전
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // http 요청에서의 권한을 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/api/user/**").permitAll() // '/api/auth/'로 시작하는 요청 모두 접근 허가 -> 이게 없으면 회원가입, 로그인을 할 수가 없음
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll() // 'GET /api/posts'로 시작하는 요청 모두 접근 허가 -> 로그인하지 않아도 게시글 read 가능
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        // 필터 관리
        // UsernamePasswordAuthenticationFilter 전에 jwtAuthorizationFilter를 거치도록 함
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}