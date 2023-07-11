package com.example.springlv_4.dto;

import com.example.springlv_4.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class PostResponseDto extends ApiResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String username;
    private List<CommentResponseDto> comments;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.username = post.getUser().getUsername();
        this.comments = post.getComments().stream()
                .map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed())
                .toList();
    }
}
