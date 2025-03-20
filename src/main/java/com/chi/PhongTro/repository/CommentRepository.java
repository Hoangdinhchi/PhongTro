package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, String> {
    @Query("SELECT c FROM Comments c WHERE c.post.post_id = :postId")
    List<Comments> findByPostId(@Param("postId") long postId);
}