package com.vweinert.fedditbackend.repository;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vweinert.fedditbackend.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}