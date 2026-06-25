package com.example.BibliotecaAPI.repository;

import com.example.BibliotecaAPI.entity.Book;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:genre IS NULL OR LOWER(b.genre) = LOWER(:genre)) AND " +
            "(:authorId IS NULL OR b.author.id = :authorId) AND " +
            "(:available IS NULL OR (:available = true AND b.availableCopies > 0) OR (:available = false AND b.availableCopies = 0))")
    Page<Book> findWithFilters(@Param("title") String title, @Param("genre") String genre,
                               @Param("authorId") Long authorId, @Param("available") Boolean available, Pageable pageable);

    boolean existsByAuthorId(Long authorId);
}