package com.vweinert.fedditbackend.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vweinert.fedditbackend.models.Comment;
import com.vweinert.fedditbackend.models.Post;
import com.vweinert.fedditbackend.models.User;
import com.vweinert.fedditbackend.exception.custom.ResourceNotFoundException;
import com.vweinert.fedditbackend.repository.CommentRepository;
import com.vweinert.fedditbackend.repository.PostRepository;
import com.vweinert.fedditbackend.repository.UserRepository;
import com.vweinert.fedditbackend.service.inter.CommentService;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final SanitizerUtils sanitizerUtils;
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    public CommentServiceImpl (CommentRepository commentRepository, UserRepository userRepository,PostRepository postRepository, SanitizerUtils sanitizerUtils) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.sanitizerUtils = sanitizerUtils;
        logger.debug("comment service initialized");
    }
    public Comment createComment(long userId, long postId, Comment commentRequest) throws Exception{
        Optional<User> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(postId);
        if (!user.isPresent()) {
            logger.error("user that doesn't exist tried to put comment on post");
            throw new ResourceNotFoundException("tried to post comment on  non-existent userId");
        } else if (!post.isPresent()) {
            logger.warn("userid {} tried to post comment on post that doesn't exist {}",userId,postId);
            throw new ResourceNotFoundException("tried to post comment on  non-existent postId");
        } else {
            Comment saving = Comment.builder()
                    .user(user.get())
                    .post(post.get())
                    .content(commentRequest.getContent())
                    .build();
            Comment saved = commentRepository.save(saving);

            return SanitizerUtils.sanitizeComment(saved);
        }

    }
    public Comment getComment(long commentId) throws Exception{
        Optional<Comment> comment = commentRepository.getCommentById(commentId);
        if (!comment.isPresent()) {
            logger.warn("tried to get comment id {} that doesn't exist",commentId);
            throw new ResourceNotFoundException("tried to find comment on bad ID");
        } else {
            return sanitizerUtils.sanitizeComment(comment.get());
        }
    }
    public Comment updateComment(long userId,Comment commentRequest) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        Optional<Comment> comment = commentRepository.findById(commentRequest.getId());
        if (!user.isPresent()) {
            logger.error("user that doesn't exist tried to put comment");
            throw new ResourceNotFoundException("tried to find user on bad ID");
        } else if (!comment.isPresent()) {
            logger.warn("userid {} tried to put on comment that doesn't exist comment: {}", userId, commentRequest);
            throw new ResourceNotFoundException("tried to find comment on bad ID");
        } else if (comment.get().getUser().getId() != userId) {
            logger.error("userId {} tried to put on another users comment", userId);
            throw new Exception("tried to update another users comment");
        } else if (comment.get().getContent().equals(commentRequest.getContent())) {
            logger.warn("userId {} tried to update comment with no update comment {}",userId,comment);
            throw new Exception("user made no updates");
        } else {
            comment.get().setModifiedAt(LocalDateTime.now());
            comment.get().setContent(commentRequest.getContent());
            Comment saved = commentRepository.save(comment.get());
            return sanitizerUtils.sanitizeComment(saved);
        }
    }
    public Comment deleteComment(long userId, long commentId) throws Exception{
        Optional<User> user = userRepository.findById(userId);
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (!user.isPresent()) {
            logger.error("user that doesn't exist tried to delete comment");
            throw new ResourceNotFoundException("tried to find user on bad ID");
        } else if (!comment.isPresent()) {
            logger.warn("userid {} tried to delete a comment that doesn't exist",userId);
            throw new ResourceNotFoundException("tried to find comment on bad ID");
        } else if (comment.get().getUser().getId() != userId) {
            logger.error("userId {} tried to delete on another users comment",userId);
            throw new Exception("tried to delete another users comment");
        } else {
            comment.get().setDeleted(true);
            Comment saved = commentRepository.save(comment.get());
            return sanitizerUtils.sanitizeComment(saved);
        }
    }
}
