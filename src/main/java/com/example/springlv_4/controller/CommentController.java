package com.example.springlv_4.controller;

import com.example.springlv_4.dto.ApiResponseDto;
import com.example.springlv_4.dto.CommentRequestDto;
import com.example.springlv_4.dto.CommentResponseDto;
import com.example.springlv_4.security.UserDetailsImpl;
import com.example.springlv_4.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> createComment (@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto requestDto) {
        // 댓글 작성
        CommentResponseDto responseDto = commentService.createComment(userDetails.getUser(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<ApiResponseDto> updateComment (@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @RequestBody CommentRequestDto requestDto) {
        try {
            // 댓글 업데이트
            CommentResponseDto result = commentService.updateComment(id, requestDto, userDetails.getUser());
            // 업데이트 후 httpStatus code랑 결과 리턴
            return ResponseEntity.ok().body(result);
        } catch (RejectedExecutionException e) {
            // commentService.updateComment에서 권한이 없거나 해당 댓글 작성자가 아니면 수행
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 수정 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponseDto> deleteComment (@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        try {
            commentService.deleteComment(id, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("해당하는 댓글이 삭제되었습니다.", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }
}
