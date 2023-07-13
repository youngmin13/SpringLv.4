package com.example.springlv_4.service;

import com.example.springlv_4.dto.PostListResponseDto;
import com.example.springlv_4.dto.PostRequestDto;
import com.example.springlv_4.dto.PostResponseDto;
import com.example.springlv_4.entity.Post;
import com.example.springlv_4.entity.User;
import com.example.springlv_4.entity.UserRoleEnum;
import com.example.springlv_4.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        // 게시글 생성
        Post post = new Post (requestDto);
        // 유저 정보 set
        post.setUser(user);
        // 저장
        postRepository.save(post);

        return new PostResponseDto(post);
    }

    public PostListResponseDto getPosts() {
        // stream을 사용해서 게시글 전부 받아와서 List로 저장
        List<PostResponseDto> postList = postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new PostListResponseDto(postList);
    }

    public PostResponseDto getPostById(Long id) {
        // id로 게시글 찾기
        Post post = findPostById(id);
        
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(User user, Long id, PostRequestDto requestDto) {
        // id로 게시글 찾기
        Post post = findPostById(id);
        
        // 관리자가 아니거나 게시글을 작성한 유저가 아니면
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().equals(user))) {
            throw new RejectedExecutionException("작성자만 수정 할 수 있습니다.");
        }

        // 게시글 제목 업데이트
        post.setTitle(requestDto.getTitle());
        // 게시글 내용 업데이트
        post.setContent(requestDto.getContent());

        return new PostResponseDto(post);
    }

    public void deletePost(User user, Long id) {
        Post post = findPostById(id);

        // 관리자가 아니거나 게시글을 작성한 유저가 아니면
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().equals(user))) {
            throw new RejectedExecutionException("작성자만 수정 할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    // id로 게시글 찾는 메서드
    public Post findPostById (Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }
}
