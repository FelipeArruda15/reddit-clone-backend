package com.felipearruda.redditclone.repository;

import com.felipearruda.redditclone.model.Post;
import com.felipearruda.redditclone.model.Subreddit;
import com.felipearruda.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository  extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
