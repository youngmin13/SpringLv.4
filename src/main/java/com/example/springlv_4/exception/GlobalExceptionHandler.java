package com.example.springlv_4.exception;

import com.example.springlv_4.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.RejectedExecutionException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResponseDto> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        ApiResponseDto responseDto = new ApiResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // HTTP body
                responseDto,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<ApiResponseDto> nullPointerExceptionHandler(NullPointerException ex) {
        ApiResponseDto responseDto = new ApiResponseDto(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(
                // HTTP body
                responseDto,
                // HTTP status code
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler({RejectedExecutionException.class})
    public ResponseEntity<ApiResponseDto> RejectedExecutionException(RejectedExecutionException ex) {
        ApiResponseDto responseDto = new ApiResponseDto(ex.getMessage(), HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(
                // HTTP body
                responseDto,
                // HTTP status code
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponseDto> MethodArgumentNotValidException(RejectedExecutionException ex) {
        ApiResponseDto responseDto = new ApiResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // HTTP body
                responseDto,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }
}