package com.felipearruda.redditclone.service;

import com.felipearruda.redditclone.dto.CommentsDto;
import com.felipearruda.redditclone.exception.PostNotFoundException;
import com.felipearruda.redditclone.mapper.CommentMapper;
import com.felipearruda.redditclone.model.NotificationEmail;
import com.felipearruda.redditclone.model.Post;
import com.felipearruda.redditclone.model.User;
import com.felipearruda.redditclone.repository.CommentRepository;
import com.felipearruda.redditclone.repository.PostRepository;
import com.felipearruda.redditclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentService {

    private static final String POST_URL = "";
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final AuthService authService;
    private final EmailContentBuilder emailContentBuilder;
    private final EmailService emailService;

    public void createComment(CommentsDto commentsDto){
        Post post = postRepository.findById(commentsDto.getPostId())
                        .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        commentRepository.save(commentMapper.map(commentsDto, post, authService.getCurrentUser()));

        String message = emailContentBuilder.build(post.getUser().getUserId() + "escreveu um comentário no seu post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    public List<CommentsDto> getCommentByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentsDto> getCommentsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    private void sendCommentNotification(String message, User user) {
        emailService.sendEmail(new NotificationEmail(user.getUsername() +  "Comentou no seu post", user.getEmail(), message));
    }
}
