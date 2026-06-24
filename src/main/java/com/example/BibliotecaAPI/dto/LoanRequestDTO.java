// LoanRequestDTO.java
package com.example.BibliotecaAPI.dto;
import jakarta.validation.constraints.*;

public record LoanRequestDTO(
        @NotNull Long bookId,
        @NotBlank @Email String borrowerEmail
) {}