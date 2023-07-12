package com.example.springlv_4.repository;

import com.example.springlv_4.entity.Likes;
import com.example.springlv_4.entity.Post;
import com.example.springlv_4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByPostAndUser(Long post_id, Long user_id);

    Optional<Likes> findByCommentAndUser(Long comment_id, Long user_id);
}
