package com.example.gamecommunity.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.gamecommunity.common.entity.BaseEntity;
import com.example.gamecommunity.domain.post.entity.Post;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
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

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts = new ArrayList<>();

	@Builder
	public User(String email, String password, String nickname) {

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
