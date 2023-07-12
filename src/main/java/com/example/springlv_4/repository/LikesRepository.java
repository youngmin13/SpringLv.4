package com.example.springlv_4.repository;

import com.example.springlv_4.entity.Comment;
import com.example.springlv_4.entity.Likes;
import com.example.springlv_4.entity.Post;
import com.example.springlv_4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByPostAndUser(Post post, User user);

    Optional<Likes> findByCommentAndUser(Comment comment, User user);
}
