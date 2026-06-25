// BookRequestDTO.java
package com.example.BibliotecaAPI.dto;
import jakarta.validation.constraints.*;

public record BookRequestDTO(
        @NotBlank String title,
        @NotBlank String genre,
        @NotNull @Min(0) Integer availableCopies,
        @NotNull Long authorId
) {}