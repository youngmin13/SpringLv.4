package com.example.springlv_4.controller;

import com.example.springlv_4.dto.ApiResponseDto;
import com.example.springlv_4.security.UserDetailsImpl;
import com.example.springlv_4.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikesController {

    public final LikesService likesService;

    @PostMapping("/posts/{id}/like")
    public ResponseEntity<ApiResponseDto> likesPost (@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDto res = likesService.likesPost(id, userDetails.getUser());
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/comments/{id}/like")
    public ResponseEntity<ApiResponseDto> likesComment (@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApiResponseDto res = likesService.likesComment(id, userDetails.getUser());
        return ResponseEntity.ok().body(res);
    }
}
