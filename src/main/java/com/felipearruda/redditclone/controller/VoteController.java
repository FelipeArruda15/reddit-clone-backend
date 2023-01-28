package com.felipearruda.redditclone.controller;

import com.felipearruda.redditclone.dto.VoteDto;
import com.felipearruda.redditclone.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
@Tag(name = "Voto", description = "API para os serviços de votos")
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    @Operation(summary = "Votar", description = "Adiciona um voto em algum Post")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto) {
        voteService.vote(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
