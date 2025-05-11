package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Posts, String>, JpaSpecificationExecutor<Posts> {
    @Query("SELECT p FROM Posts p WHERE p.user.user_id = :userId ORDER BY p.createdAt DESC")
    List<Posts> findByUserId(@Param("userId") long userId);
}
