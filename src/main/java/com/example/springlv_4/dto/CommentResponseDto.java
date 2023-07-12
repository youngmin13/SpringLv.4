package com.example.springlv_4.dto;

import com.example.springlv_4.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto extends ApiResponseDto {
    private String body;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCount;

    public CommentResponseDto(Comment comment) {
        super();
        this.body = comment.getBody();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCount = comment.getLikeCount();
    }
}