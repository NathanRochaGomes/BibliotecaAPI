// LoanResponseDTO.java
package com.example.BibliotecaAPI.dto;
import java.time.LocalDate;

public record LoanResponseDTO(Long id, String bookTitle, String borrowerEmail, LocalDate loanDate, LocalDate returnDate, String status) {}