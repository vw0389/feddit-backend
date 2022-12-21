package com.vweinert.fedditbackend.service.inter;
import java.util.List;
import java.util.Optional;

import com.vweinert.fedditbackend.entities.Post;
import com.vweinert.fedditbackend.exception.ResourceNotFoundException;
public interface PostService {
    Optional<Post> getMostRecentPost();
    List<Post> getTenMostRecentPosts();
    Post createPost(Post post);
    Post updatePost(long postId, long userId, Post post) throws Exception;
    void deletePost(long postId, long userId) throws Exception;
	Post getPostById(long id) throws ResourceNotFoundException;
}
