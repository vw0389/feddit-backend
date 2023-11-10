package com.vweinert.fedditbackend.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vweinert.fedditbackend.models.Comment;
import com.vweinert.fedditbackend.models.Post;
import com.vweinert.fedditbackend.models.User;
import com.vweinert.fedditbackend.exception.custom.ResourceNotFoundException;
import com.vweinert.fedditbackend.repository.PostRepository;
import com.vweinert.fedditbackend.service.inter.PostService;
import com.vweinert.fedditbackend.repository.UserRepository;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SanitizerUtils sanitizerUtils;
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, SanitizerUtils sanitizerUtils){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.sanitizerUtils = sanitizerUtils;
        logger.debug("post service initialized");
    }
    @Override
	public Post createPost(long userId, Post post)throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
            post.setUser(SanitizerUtils.sanitizedUser(user.get()));
            return postRepository.saveAndFlush(post);

        } else {
            logger.error("userid {} that doesn't exist tried to post post {}",userId,post);
            throw new ResourceNotFoundException("user not found");
        }

	}
    @Override
    public Post getPostById(long postId) throws Exception {
        Optional<Post> result = postRepository.findById(postId);
        if(result.isPresent() && !result.get().getDeleted()) {
            return SanitizerUtils.sanitizePost(result.get());
        }else {
            logger.warn("tried to get postId {}, not in DB",postId);
            throw new ResourceNotFoundException("post with that id not found");
        }
    }
    @Override
	public Post deletePost(long userId, long postId) throws Exception {
		Post post = postRepository.findById(postId)
				.orElseThrow(ResourceNotFoundException::new);
		if (post.getUser().getId() != userId){
            logger.error("userid {} tried to delete another users post",userId);
            throw new Exception();
        }
		post.setDeleted(true);
        postRepository.save(post);
        return SanitizerUtils.sanitizePost(post);
	}

    @Override
    public Optional<Post> getMostRecentPost() {
        Optional<Post> result = postRepository.findMostRecent();
		if(result.isPresent()) {
            return Optional.of(SanitizerUtils.sanitizePost(result.get()));
		}else {
			return result;
		}
    }

    @Override
    public List<Post> getTenMostRecentPosts() {
        List<Post> results = postRepository.findTenMostRecent();
        List<Post> returning = new ArrayList<>();
        for(Post post: results){
            Post current = SanitizerUtils.sanitizePost(post);
            returning.add(current);
        }
        return returning;
    }

    @Override
    public Post updatePost(long userId, long postId, Post post) throws Exception {
        Optional<Post> optPostFromRepo = postRepository.findById(postId);
        if (optPostFromRepo.isEmpty()) {
            throw new ResourceNotFoundException();
        } else if (optPostFromRepo.get().getUser().getId() != userId) {
            throw new IllegalAccessException("tried to put on anther users post");
        } else if (optPostFromRepo.get().getDeleted()) {
            throw new Exception("post is deleted ");
        } else {
            Post postFromRepo = optPostFromRepo.get();
            if (postFromRepo.getTitle().equals(post.getTitle()) && postFromRepo.getContent().equals(post.getContent())) {
                logger.warn("userId {} put on post with no updates {}",userId,post);
                throw new Exception("Nothing changed");
            } else {
                Post postFromRepos = optPostFromRepo.get();
                postFromRepos.setTitle(post.getTitle());
                postFromRepos.setContent(post.getContent());
                Post saved = postRepository.saveAndFlush(postFromRepos);
                return SanitizerUtils.sanitizePost(saved);
            }
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


}
