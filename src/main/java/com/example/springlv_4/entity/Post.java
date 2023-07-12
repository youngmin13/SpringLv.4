package com.example.springlv_4.entity;

import com.example.springlv_4.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.IntSet;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post")
public class Post extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    // 해당 게시글의 좋아요 숫자 합
    @Column(name = "likes", nullable = false)
    private int likeCount = 0;

    // 게시글이 삭제되면 해당 게시글의 좋아요 삭제
    // Like에 대한 정보가 담겨 있어야 하기 때문에, likesCount 말고 별도로 리스트도 존재해야함
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Likes> likesList = new ArrayList<>();

    public Post (PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public Post (PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public void setUser (User user) {
        this.user = user;
    }

    public void increaseLikeCount(){
        this.likeCount++;
    }

    public void decreaseLikeCount(){
        this.likeCount--;
    }
}
