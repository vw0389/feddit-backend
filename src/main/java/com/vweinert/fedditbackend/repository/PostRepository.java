package com.vweinert.fedditbackend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vweinert.fedditbackend.models.Post;
@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
}
