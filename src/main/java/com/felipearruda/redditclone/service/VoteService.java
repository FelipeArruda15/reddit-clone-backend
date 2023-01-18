package com.felipearruda.redditclone.service;

import com.felipearruda.redditclone.dto.VoteDto;
import com.felipearruda.redditclone.exception.PostNotFoundException;
import com.felipearruda.redditclone.exception.SpringRedditException;
import com.felipearruda.redditclone.mapper.VoteMapper;
import com.felipearruda.redditclone.model.Post;
import com.felipearruda.redditclone.model.User;
import com.felipearruda.redditclone.model.Vote;
import com.felipearruda.redditclone.repository.PostRepository;
import com.felipearruda.redditclone.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.felipearruda.redditclone.model.enums.VoteType.UPVOTE;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final VoteMapper voteMapper;

    public void vote(VoteDto voteDto){
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post não encontrado com ID:" + voteDto.getPostId()));
        Optional<Vote> vote =  voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (vote.isPresent() &&
                voteDto.getVoteType().equals(vote.get().getVoteType())) {
            throw new SpringRedditException("Você já " + voteDto.getVoteType() + " para esse post");
        }

        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(voteMapper.map(voteDto, post, authService.getCurrentUser()));
        postRepository.save(post);
    }
}
