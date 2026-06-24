package com.example.BibliotecaAPI.repository;

import com.example.BibliotecaAPI.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    // Código que faz o filtro por nome ignorando maiúsculas/minúsculas
    Page<Author> findByNameContainingIgnoreCase(String name, Pageable pageable);
}