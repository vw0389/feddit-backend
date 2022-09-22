package com.vweinert.fedditbackend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vweinert.fedditbackend.models.Post;
@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    
}
