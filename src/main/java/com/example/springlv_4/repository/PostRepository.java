package com.example.springlv_4.repository;

import com.example.springlv_4.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
