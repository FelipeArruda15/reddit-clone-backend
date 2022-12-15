package com.felipearruda.redditclone.repository;

import com.felipearruda.redditclone.model.Post;
import com.felipearruda.redditclone.model.User;
import com.felipearruda.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
