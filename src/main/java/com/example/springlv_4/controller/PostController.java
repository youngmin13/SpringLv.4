package com.example.springlv_4.controller;

import com.example.springlv_4.dto.ApiResponseDto;
import com.example.springlv_4.dto.PostListResponseDto;
import com.example.springlv_4.dto.PostRequestDto;
import com.example.springlv_4.dto.PostResponseDto;
import com.example.springlv_4.security.UserDetailsImpl;
import com.example.springlv_4.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost (@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PostRequestDto requestDto) {
        PostResponseDto responseDto = postService.createPost(requestDto, userDetails.getUser());

        // 해당하는 리소스가 만들어졌기 때문에 201
        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping("/posts")
    public ResponseEntity<PostListResponseDto> getPosts () {
        // 리스트로 받아오기
        PostListResponseDto postListResponseDto = postService.getPosts();
        // 리스트 받아오면 해당하는 http Status Code와 해당 리스트 리턴
        return ResponseEntity.ok().body(postListResponseDto);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> getPostById (@PathVariable Long id) {
        // id로 해당 게시글 찾기
        PostResponseDto responseDto = postService.getPostById(id);
        // 찾았으면 해당하는 http Status Code와 해당 게시글 리턴
        return ResponseEntity.ok().body(responseDto);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<ApiResponseDto> updatePost (@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        // 해당하는 글을 찾아서 업데이트하는 메서드
        PostResponseDto responseDto = postService.updatePost(userDetails.getUser(), id, requestDto);
        // 찾았으면 찾았으면 해당하는 http Status Code와 해당 게시글 리턴
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponseDto> deletePost (@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        postService.deletePost(userDetails.getUser(), id);
        return ResponseEntity.ok().body(new ApiResponseDto("해당하는 게시글을 삭제하였습니다.", HttpStatus.OK.value()));
    }
}
