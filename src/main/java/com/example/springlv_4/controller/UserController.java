package com.example.springlv_4.controller;

import com.example.springlv_4.dto.ApiResponseDto;
import com.example.springlv_4.dto.AuthRequestDto;
import com.example.springlv_4.jwt.JwtUtil;
import com.example.springlv_4.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 회원 가입 메서드
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto> signUP (@Valid @RequestBody AuthRequestDto requestDto) {
        try {
            // 회원 가입
            userService.signUp(requestDto);
        } catch (IllegalArgumentException e) {
            // userService에서 중복된 username을 가지고 있어서 exception이 발생하면
            return ResponseEntity.badRequest().body(new ApiResponseDto("중복된 username입니다.", HttpStatus.BAD_REQUEST.value()));
        }
        // ok는 200, created는 201 인데 해당 요청이 성공했고, 리소스가 만들어졌다면 201
        return ResponseEntity.status(201).body(new ApiResponseDto("회원 가입 성공", HttpStatus.CREATED.value()));
    }

    // 로그인 메서드
    @PostMapping("login")
    public ResponseEntity<ApiResponseDto> login (@RequestBody AuthRequestDto requestDto, HttpServletResponse response) {
        try {
            // 로그인
            userService.login(requestDto);
        } catch (IllegalArgumentException e) {
            // userService에서 해당하는 username을 가진 사용자가 없어서 exception이 발생하면
            return ResponseEntity.badRequest().body(new ApiResponseDto("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value()));
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        // 로그인 시에 jwt가 생성되는 것에 주의
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(requestDto.getUsername(), requestDto.getRole()));

        return ResponseEntity.ok().body(new ApiResponseDto("로그인 성공", HttpStatus.CREATED.value()));
    }

}
