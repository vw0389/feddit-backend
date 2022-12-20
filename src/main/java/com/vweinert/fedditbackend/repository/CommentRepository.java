package com.vweinert.fedditbackend.repository;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vweinert.fedditbackend.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query(value="select * from comments order by created_at desc limit 10 where post_id = :id", nativeQuery = true)
    Set<Comment> findTenMostRecent(Long id);
}