package com.chi.PhongTro.repository;

import com.chi.PhongTro.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Posts, String>, JpaSpecificationExecutor<Posts> {

}
