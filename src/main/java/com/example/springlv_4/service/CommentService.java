package com.example.springlv_4.service;

import com.example.springlv_4.dto.CommentRequestDto;
import com.example.springlv_4.dto.CommentResponseDto;
import com.example.springlv_4.entity.Comment;
import com.example.springlv_4.entity.Post;
import com.example.springlv_4.entity.User;
import com.example.springlv_4.entity.UserRoleEnum;
import com.example.springlv_4.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostService postService;
    private final CommentRepository commentRepository;
    public CommentResponseDto createComment(User user, CommentRequestDto requestDto) {
        // 우선 해당 게시글이 존재하는지 확인
        Post post = postService.findPostById(requestDto.getPostId());

        // 댓글 작성
        Comment comment = new Comment(requestDto.getBody());
        comment.setPost(post);
        comment.setUser(user);

        return new CommentResponseDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당하는 댓글이 존재하지 않습니다.")
        );

        // 요청자가 운영자이거나 댓글 작성자(post.user) 와 요청자(user) 가 같은지 체크
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || comment.getUser().equals(user))) {
            // 아니면 exception throw 해서 controller에서 처리
            throw new RejectedExecutionException("작성자만 수정 할 수 있습니다.");
        }

        // 해당 댓글 수정
        comment.setBody(requestDto.getBody());

        return new CommentResponseDto(comment);
    }

    public void deleteComment(Long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당하는 댓글이 존재하지 않습니다.")
        );

        // 요청자가 운영자이거나 댓글 작성자(post.user) 와 요청자(user) 가 같은지 체크
        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || comment.getUser().equals(user))) {
            // 아니면 exception throw 해서 controller에서 처리
            throw new RejectedExecutionException("작성자만 수정 할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}
