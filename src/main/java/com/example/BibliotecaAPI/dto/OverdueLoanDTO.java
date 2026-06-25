// OverdueLoanDTO.java
package com.example.BibliotecaAPI.dto;
import java.time.LocalDate;

public record OverdueLoanDTO(Long loanId, String bookTitle, String borrowerEmail, long daysOverdue) {}