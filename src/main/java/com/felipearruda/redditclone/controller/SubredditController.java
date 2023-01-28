package com.felipearruda.redditclone.controller;

import com.felipearruda.redditclone.dto.SubredditDto;
import com.felipearruda.redditclone.service.SubredditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@RequiredArgsConstructor
@Tag(name = "Subreddit", description = "API para os serviços de subreddit")
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria um novo subreddit", description = "Cria um novo subreddit")
    @SecurityRequirement(name = "Bearer Authentication")
    public SubredditDto create(@RequestBody SubredditDto subredditDto){
        return subredditService.save(subredditDto);
    }

    @GetMapping
    @Operation(summary = "Retorna todos os subreddits", description = "Retorna todos os subreddits")
    public List<SubredditDto> getAllSubreddits() {
        return subredditService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar subreddit por Id", description = "Retorna um único subreddit",
        parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Subreddit Id")}
    )
    public SubredditDto getSubreddit(@PathVariable Long id) {
        return subredditService.getSubreddit(id);
    }
}
