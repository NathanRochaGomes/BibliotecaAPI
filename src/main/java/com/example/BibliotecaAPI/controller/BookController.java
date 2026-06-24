package com.example.BibliotecaAPI.controller;

import com.example.BibliotecaAPI.dto.*;
import com.example.BibliotecaAPI.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired private BookService bookService;

    @GetMapping
    public ResponseEntity<Page<BookResponseDTO>> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Boolean available,
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        return ResponseEntity.ok(bookService.getAll(title, genre, authorId, available, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> create(@RequestBody @Valid BookRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> update(@PathVariable Long id, @RequestBody @Valid BookRequestDTO dto) {
        return ResponseEntity.ok(bookService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}