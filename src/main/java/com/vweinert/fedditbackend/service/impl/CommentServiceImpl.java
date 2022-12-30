package com.vweinert.fedditbackend.service.impl;

import com.vweinert.fedditbackend.entities.Comment;
import com.vweinert.fedditbackend.entities.Post;
import com.vweinert.fedditbackend.entities.User;
import com.vweinert.fedditbackend.exception.ResourceNotFoundException;
import com.vweinert.fedditbackend.repository.CommentRepository;
import com.vweinert.fedditbackend.repository.PostRepository;
import com.vweinert.fedditbackend.repository.UserRepository;
import com.vweinert.fedditbackend.service.inter.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public CommentServiceImpl (CommentRepository commentRepository, UserRepository userRepository,PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }
    public Comment createComment(long userId, long postId, Comment commentRequest) throws Exception{
        Optional<User> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(postId);
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("tried to post comment on  non-existent userId");
        } else if (!post.isPresent()) {
            throw new ResourceNotFoundException("tried to post comment on  non-existent postId");
        } else {
            Comment saving = Comment.builder()
                    .user(user.get())
                    .post(post.get())
                    .content(commentRequest.getContent())
                    .build();
            Comment saved = commentRepository.save(saving);

            return sanitizeComment(saved);
        }

    }
    public Comment getComment(long commentId) throws Exception{
        Optional<Comment> comment = commentRepository.getCommentById(commentId);
        if (!comment.isPresent()) {
            throw new ResourceNotFoundException("tried to find comment on bad ID");
        } else {
            return sanitizeComment(comment.get());
        }
    }
    public Comment updateComment(long userId,Comment commentRequest) throws Exception{
        Optional<User> user = userRepository.findById(userId);
        Optional<Comment> comment = commentRepository.findById(commentRequest.getId());
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("tried to find user on bad ID");
        } else if (!comment.isPresent()) {
            throw new ResourceNotFoundException("tried to find comment on bad ID");
        } else if (comment.get().getUser().getId() != userId) {
            throw new Exception("tried to update another users comment");
        } else {
            comment.get().setModifiedAt(LocalDateTime.now());
            comment.get().setContent(commentRequest.getContent());
            Comment saved = commentRepository.save(comment.get());
            return sanitizeComment(saved);
        }
    }
    public Comment deleteComment(long userId, long commentId) throws Exception{
        Optional<User> user = userRepository.findById(userId);
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("tried to find user on bad ID");
        } else if (!comment.isPresent()) {
            throw new ResourceNotFoundException("tried to find comment on bad ID");
        } else if (comment.get().getUser().getId() != userId) {
            throw new Exception("tried to delete another users comment");
        } else {
            comment.get().setDeleted(true);
            Comment saved = commentRepository.save(comment.get());
            return sanitizeComment(saved);
        }
    }
    public User sanitizedUser(User user) {
        return User
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .deleted(user.getDeleted())
                .build();
    }
    public Comment sanitizeComment(Comment comment) {
        return Comment.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .deleted(comment.getDeleted())
                .user(sanitizedUser(comment.getUser()))
                .build();
    }
}
