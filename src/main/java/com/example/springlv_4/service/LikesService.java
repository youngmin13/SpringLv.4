package com.example.springlv_4.service;

import com.example.springlv_4.dto.ApiResponseDto;
import com.example.springlv_4.entity.Comment;
import com.example.springlv_4.entity.Likes;
import com.example.springlv_4.entity.Post;
import com.example.springlv_4.entity.User;
import com.example.springlv_4.jwt.JwtUtil;
import com.example.springlv_4.repository.CommentRepository;
import com.example.springlv_4.repository.LikesRepository;
import com.example.springlv_4.repository.PostRepository;
import com.example.springlv_4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ApiResponseDto likesPost(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );

        // 현재 사용자가 해당 게시글에 좋아요를 체크했는지 확인해야한다.
        Optional<Likes> isUserCheckPostLikeBefore = likesRepository.findByPostAndUser(id, user.getId());

        // 좋아요 체크가 되어있지 않은 상태라면
        if (!isUserCheckPostLikeBefore.isPresent()) {
            // .get을 통해서 Optional 객체에 접근 가능
            // isPresent() 메소드를 사용하여 Optional 객체에 저장된 값이 null인지 아닌지를 먼저 확인한 후 호출
            likesRepository.save(new Likes(user, post));
            post.increaseLikeCount();
            return new ApiResponseDto("해당 게시글에 좋아요를 눌렀습니다.", HttpStatus.OK.value());
        }
        else {
            likesRepository.delete(isUserCheckPostLikeBefore.get());
            post.decreaseLikeCount();
            return new ApiResponseDto("해당 게시글에 좋아요가 취소되었습니다.", HttpStatus.OK.value());
        }
    }

    @Transactional
    public ApiResponseDto likesComment(Long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다.")
        );

        // 현재 사용자가 해당 게시글에 좋아요를 체크했는지 확인해야한다.
        Optional<Likes> isUserCheckCommentLikeBefore = likesRepository.findByCommentAndUser(id, user.getId());

        // 좋아요 체크가 되어있지 않은 상태라면
        if (!isUserCheckCommentLikeBefore.isPresent()) {
            // .get을 통해서 Optional 객체에 접근 가능
            // isPresent() 메소드를 사용하여 Optional 객체에 저장된 값이 null인지 아닌지를 먼저 확인한 후 호출
            likesRepository.save(new Likes(user, comment));
            comment.increaseLikeCount();
            return new ApiResponseDto("해당 댓글에 좋아요를 눌렀습니다.", HttpStatus.OK.value());
        }
        else {
            likesRepository.delete(isUserCheckCommentLikeBefore.get());
            comment.decreaseLikeCount();
            return new ApiResponseDto("해당 댓글에 좋아요가 취소되었습니다.", HttpStatus.OK.value());
        }
    }
}
