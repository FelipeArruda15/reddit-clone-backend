package com.felipearruda.redditclone.controller;

import com.felipearruda.redditclone.dto.CommentsDto;
import com.felipearruda.redditclone.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comentário", description = "API para os serviços de comentário")
public class CommentsController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Realiza um comentário", description = "Adiciona um comentário em algum post")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        commentService.createComment(commentsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/by-post/{postId}")
    @Operation(summary = "Buscar comentários por post", description = "Retorna todos os comentários referente ao Post passado como parâmetro",
        parameters = {@Parameter(in = ParameterIn.PATH, name = "postId", description = "Post Id")}
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentByPost(postId));
    }

    @GetMapping("/by-user/{userName}")
    @Operation(summary = "Buscar comentários por usuário", description = "Retorna todos os comentários referente ao Usuário passado como parâmetro",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "userName", description = "Username")}
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<CommentsDto>> getAllCommentsByUser(@PathVariable String userName) {
        return ResponseEntity.ok(commentService.getCommentsByUser(userName));
    }

}
