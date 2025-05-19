package com.example.gamecommunity.domain.post.repository;

import com.example.gamecommunity.domain.post.entity.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findPostById(Long id);

    List<Post> findAllById(Long id);

    Page<Post> findAllByTitleContaining(String title, Pageable pageable);
}
