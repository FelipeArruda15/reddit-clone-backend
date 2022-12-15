package com.felipearruda.redditclone.repository;

import com.felipearruda.redditclone.model.Comment;
import com.felipearruda.redditclone.model.Post;
import com.felipearruda.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}