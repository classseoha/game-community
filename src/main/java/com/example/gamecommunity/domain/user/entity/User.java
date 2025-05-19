package com.example.gamecommunity.domain.user.entity;

import com.example.gamecommunity.common.entity.BaseEntity;
import com.example.gamecommunity.domain.post.entity.Post;
import com.example.gamecommunity.domain.user.dto.requestdto.UserUpdateRequestDto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "'user'")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Builder
    public User(String email, String password, String nickname){

        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void updatePassword(String newPassword) {

        this.password = newPassword;
    }

    public void updateNickname(String newNickname) {

        this.nickname = newNickname;
    }

}
