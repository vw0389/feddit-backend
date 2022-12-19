package com.vweinert.fedditbackend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vweinert.fedditbackend.entities.Comment;
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    
}