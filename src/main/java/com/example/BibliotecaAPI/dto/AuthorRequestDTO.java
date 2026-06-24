package com.example.BibliotecaAPI.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthorRequestDTO(
        @NotBlank(message = "O nome do autor não pode ser vazio.")
        String name
) {}