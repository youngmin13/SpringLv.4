package com.example.springlv_4.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "likes")
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Likes (User user, Post post) {
        setUser(user);
        setPost(post);
    }

    public Likes (User user, Comment comment) {
        setUser(user);
        setComment(comment);
    }

    // 해당 Likes 객체가 post 객체의 likes 리스트에 포함되어 있는지 확인하고
    // 포함되어 있지 않을 경우 post 객체의 likes 리스트에 현재 Like 객체를 추가해줌
    // Likes 객체와 Post 객체 사이에 관계설정
    public void setPost (Post post) {
        this.post = post;

        if (!post.getLikesList().contains(this))    post.getLikesList().add(this);
    }

    public void setComment (Comment comment) {
        this.comment = comment;

        if (!comment.getLikesList().contains(this))    comment.getLikesList().add(this);
    }

    public void setUser (User user) {
        this.user = user;
    }
}
