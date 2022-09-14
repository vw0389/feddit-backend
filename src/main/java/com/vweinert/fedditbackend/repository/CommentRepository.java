package com.vweinert.fedditbackend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vweinert.fedditbackend.models.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    
}