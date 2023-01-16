package com.felipearruda.redditclone.controller;

import com.felipearruda.redditclone.dto.SubredditDto;
import com.felipearruda.redditclone.service.SubredditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@RequiredArgsConstructor
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubredditDto create(@RequestBody SubredditDto subredditDto){
        return subredditService.save(subredditDto);
    }

    @GetMapping
    public List<SubredditDto> getAllSubreddits() {
        return subredditService.getAll();
    }

    @GetMapping("/{id}")
    public SubredditDto getSubreddit(@PathVariable Long id) {
        return subredditService.getSubreddit(id);
    }

}
