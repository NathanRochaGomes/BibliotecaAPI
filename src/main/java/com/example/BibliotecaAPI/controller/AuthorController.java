package com.example.BibliotecaAPI.controller;

import com.example.BibliotecaAPI.entity.Author;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.net.URI;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // GET /authors?name=&page=0&size=10&sort=name,asc
    @GetMapping
    public ResponseEntity<Page<AuthorResponseDTO>> listAll(
            @RequestParam(required = false) String name,
            Pageable pageable) {
        Page<AuthorResponseDTO> result = authorService.findAll(name, pageable);
        return ResponseEntity.ok(result);
    }

    //GET /author/{id}
    @GetMapping('/{id}')
    public ResponseEntity<AuthorResponseDTO> findById(@PathVariable long id) {
        AuthorResponseDTO author = authorService.findById(id);
        return ResponseEntity.ok(author);
    }

    // POST /authors → 201 Created
    @PostMapping
    public ResponseEntity<AuthorResponseDTO> create(@RequestBody @valid AuthorRequestDTO dto) {
        AuthorResponseDTO created = authorService.create(dto);
        URI location = URI.create("/authors/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    // PUT /authors/{id} → 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid AuthorRequestDTO dto) {

        AuthorResponseDTO updated = authorService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE /authors/{id} → 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
