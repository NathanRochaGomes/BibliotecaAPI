// BookResponseDTO.java
package com.example.BibliotecaAPI.dto;

public record BookResponseDTO(Long id, String title, String genre, Integer availableCopies, String authorName) {}