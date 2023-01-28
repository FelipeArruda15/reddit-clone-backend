package com.felipearruda.redditclone.controller;

import com.felipearruda.redditclone.dto.PostRequest;
import com.felipearruda.redditclone.dto.PostResponse;
import com.felipearruda.redditclone.service.PostService;
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
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Tag(name = "Post", description = "API para os serviços de Post")
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "Cria um novo post", description = "Adiciona um novo post a um subreddit")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity createPost(@RequestBody PostRequest postRequest) {
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Retorna todos os posts", description = "Retorna todos os posts")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar post por id", description = "Retorna um único Post",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Post Id")}
    )
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @GetMapping("/by-subreddit/{id}")
    @Operation(summary = "Buscar posts por subreddit", description = "Retorna todos os posts criados no subreddit passado como parâmetro",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "id", description = "Subreddit Id")}
    )
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostsBySubreddit(id));
    }

    @GetMapping("/by-user/{name}")
    @Operation(summary = "Buscar posts por usuário", description = "Retorna todos os posts criados pelo usuário passado como parâmetro",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "name", description = "Username")}
    )
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable(name = "name") String username) {
        return ResponseEntity.ok(postService.getPostsByUsername(username));
    }
}
