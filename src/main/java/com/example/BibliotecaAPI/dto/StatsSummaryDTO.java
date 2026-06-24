// StatsSummaryDTO.java
package com.example.BibliotecaAPI.dto;

public record StatsSummaryDTO(long totalBooks, long totalAuthors, long activeLoans, long overdueLoans) {}