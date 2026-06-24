package com.example.BibliotecaAPI.repository;

import com.example.BibliotecaAPI.entity.Loan;
import com.example.BibliotecaAPI.dto.TopBookDTO;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("SELECT l FROM Loan l WHERE " +
            "(:status IS NULL OR l.status = :status) AND " +
            "(:bookId IS NULL OR l.book.id = :bookId) AND " +
            "(:borrowerEmail IS NULL OR l.borrowerEmail = :borrowerEmail)")
    Page<Loan> findWithFilters(@Param("status") String status, @Param("bookId") Long bookId,
                               @Param("borrowerEmail") String borrowerEmail, Pageable pageable);

    boolean existsByBookIdAndStatus(Long bookId, String status);

    long countByStatus(String status);

    @Query("SELECT new com.example.BibliotecaAPI.dto.TopBookDTO(l.book.title, COUNT(l)) " +
            "FROM Loan l GROUP BY l.book.title ORDER BY COUNT(l) DESC")
    List<TopBookDTO> findTopBooks(Pageable pageable);

    List<Loan> findByStatus(String status);
}