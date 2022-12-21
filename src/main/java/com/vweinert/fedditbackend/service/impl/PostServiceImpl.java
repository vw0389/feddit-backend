package com.vweinert.fedditbackend.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vweinert.fedditbackend.entities.Comment;
import com.vweinert.fedditbackend.entities.Post;
import com.vweinert.fedditbackend.entities.User;
import com.vweinert.fedditbackend.exception.ResourceNotFoundException;
import com.vweinert.fedditbackend.repository.PostRepository;
import com.vweinert.fedditbackend.service.inter.PostService;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

	public PostServiceImpl(PostRepository postRepository) {
		super();
		this.postRepository = postRepository;

	}


    @Override
	public Post createPost(Post post) {
		return postRepository.save(post);
	}

    @Override
	public void deletePost(long postId, long userId) throws Exception {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException());
		if (post.getUser().getId() != userId){
            throw new Exception();
        }
		post.setDeleted(true);
        postRepository.save(post);
	}

    @Override
    public Post getPostById(long id) throws ResourceNotFoundException {
		Optional<Post> result = postRepository.findById(id);
		if(result.isPresent()) {
			return this.sanitizePost(result.get());
		}else {
			throw new ResourceNotFoundException();
		}
	}

    @Override
    public Optional<Post> getMostRecentPost() {
        Optional<Post> result = postRepository.findMostRecent();
		if(result.isPresent()) {
            Optional<Post> sanitized = Optional.of(this.sanitizePost(result.get()));
			return sanitized;
		}else {
			return result;
		}
    }

    @Override
    public List<Post> getTenMostRecentPosts() {
        List<Post> results = postRepository.findTenMostRecent();
        List<Post> returning = new ArrayList<>();
        for(Post post: results){
            Post current = sanitizePost(post);
            returning.add(current);
        }
        return returning;
    }

    @Override
    public Post updatePost(long postId, long userId, Post post) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    private Post sanitizePost(Post post) {
        List<Comment> comments = new ArrayList<>();
        for(Comment comment:post.getComments()) {
            User userComment = this.sanitizedUser(comment.getUser());
            Comment newComment = Comment
                .builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .deleted(comment.getDeleted())
                .user(userComment)
                .build();
            comments.add(newComment);
        }
        User user = this.sanitizedUser(post.getUser());
        Post returning = Post
            .builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .createdAt(post.getCreatedAt())
            .modifiedAt(post.getModifiedAt())
            .deleted(post.getDeleted())
            .user(user)
            .comments(comments)
            .build();
        return returning;
    }
    
    private User sanitizedUser(User user) {
        return User
            .builder()
            .id(user.getId())
            .username(user.getUsername())
            .deleted(user.getDeleted())
            .build();
    }
}
