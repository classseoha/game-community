package com.example.gamecommunity.domain.post.entity;

import com.example.gamecommunity.common.entity.BaseEntity;
import com.example.gamecommunity.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User users;

    public Post(String title, String contents, User users) {
        this.title = title;
        this.contents = contents;
        this.users = users;
    }

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

}
