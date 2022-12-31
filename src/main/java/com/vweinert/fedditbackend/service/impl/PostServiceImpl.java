package com.vweinert.fedditbackend.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vweinert.fedditbackend.entities.Comment;
import com.vweinert.fedditbackend.entities.Post;
import com.vweinert.fedditbackend.entities.User;
import com.vweinert.fedditbackend.exception.ResourceNotFoundException;
import com.vweinert.fedditbackend.repository.PostRepository;
import com.vweinert.fedditbackend.service.inter.PostService;
import com.vweinert.fedditbackend.repository.UserRepository;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        logger.debug("post service initialized");
    }
    @Override
	public Post createPost(long userId, Post post)throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
            post.setUser(this.sanitizedUser(user.get()));
            return postRepository.save(post);

        } else {
            logger.error("userid {} that doesn't exist tried to post post {}",userId,post);
            throw new ResourceNotFoundException("user not found");
        }

	}
    @Override
    public Post getPostById(long postId) throws Exception {
        Optional<Post> result = postRepository.findById(postId);
        if(result.isPresent() && !result.get().getDeleted()) {
            return this.sanitizePost(result.get());
        }else {
            logger.warn("tried to get postId {}, not in DB",postId);
            throw new ResourceNotFoundException("post with that id not found");
        }
    }
    @Override
	public Post deletePost(long userId, long postId) throws Exception {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException());
		if (post.getUser().getId() != userId){
            logger.error("userid {} tried to delete another users post",userId);
            throw new Exception();
        }
		post.setDeleted(true);
        postRepository.save(post);
        return this.sanitizePost(post);
	}




    @Override
    public Optional<Post> getMostRecentPost() {
        Optional<Post> result = postRepository.findMostRecent();
		if(result.isPresent()) {
            return Optional.of(this.sanitizePost(result.get()));
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
    public Post updatePost(long userId, long postId, Post post) throws Exception {
        Optional<Post> postFromRepo = postRepository.findById(postId);
        if (postFromRepo.isPresent() && postFromRepo.get().getUser().getId() == userId && !postFromRepo.get().getDeleted()) {
            if(postFromRepo.get().getTitle().equals(post.getTitle()) && postFromRepo.get().getContent().equals(post.getContent())) {
                logger.warn("userId {} put on post with no updates {}",userId,post);
                throw new Exception("Nothing changed");
            } else {
                postFromRepo.get().setTitle(post.getTitle());
                postFromRepo.get().setContent(post.getContent());
                postFromRepo.get().setModifiedAt(LocalDateTime.now());
                Post saved = postRepository.save(postFromRepo.get());
                return this.sanitizePost(saved);
            }
        } else {
            logger.error("userId {} tried to put on postId {}, post doesn't exist or post doesn't belong to user, or is deleted post {}",userId,postId,post);
            throw new Exception("post does not belong to user or is not found or is deleted ");
        }
    }

    public boolean isPostDeleted(Post post) throws Exception {
        if (post.getDeleted()) {
            return true;
        } else {
            Optional<Post> postFromRepo = postRepository.findById(post.getId());
            if (postFromRepo.isPresent()){
                if (postFromRepo.get().getDeleted()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new ResourceNotFoundException("unable to find post");
            }
        }
    }
    public Post sanitizePost(Post post) {
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
        return Post
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
    }
    
    public User sanitizedUser(User user) {
        return User
            .builder()
            .id(user.getId())
            .username(user.getUsername())
            .deleted(user.getDeleted())
            .build();
    }
}
