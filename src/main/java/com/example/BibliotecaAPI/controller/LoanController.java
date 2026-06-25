package com.example.BibliotecaAPI.controller;

import com.example.BibliotecaAPI.dto.*;
import com.example.BibliotecaAPI.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanController {
    @Autowired private LoanService loanService;

    @GetMapping
    public ResponseEntity<Page<LoanResponseDTO>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) String borrowerEmail,
            @PageableDefault(size = 10, sort = "loanDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(loanService.getAll(status, bookId, borrowerEmail, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<LoanResponseDTO> create(@RequestBody @Valid LoanRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.create(dto));
    }

    @PutMapping("/{id}") // Rota para atualizar/registrar devolução
    public ResponseEntity<LoanResponseDTO> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.returnBook(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        loanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}