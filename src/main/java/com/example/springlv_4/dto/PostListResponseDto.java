package com.example.springlv_4.dto;

import lombok.Getter;

import java.util.List;

// 게시글 리스트만 받아오도록 따로 만든 dto
@Getter
public class PostListResponseDto {
    private List<PostResponseDto> postsList;

    public PostListResponseDto(List<PostResponseDto> postList) {
        this.postsList = postList;
    }
}
