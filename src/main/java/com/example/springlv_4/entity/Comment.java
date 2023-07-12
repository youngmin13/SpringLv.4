package com.example.springlv_4.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private int likeCount = 0;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Likes> likesList = new ArrayList<>();

    public Comment (String body) {
        this.body = body;
    }

    public void setPost (Post post) {
        this.post = post;
    }

    public void setUser (User user) {
        this.user = user;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void increaseLikeCount(){
        this.likeCount++;    }

    public void decreaseLikeCount(){
        this.likeCount--;
    }

}
