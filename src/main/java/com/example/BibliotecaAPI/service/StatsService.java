package com.example.BibliotecaAPI.service;

import com.example.BibliotecaAPI.dto.*;
import com.example.BibliotecaAPI.entity.Loan;
import com.example.BibliotecaAPI.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsService {
    @Autowired private BookRepository bookRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private LoanRepository loanRepository;

    public StatsSummaryDTO getSummary() {
        long books = bookRepository.count();
        long authors = authorRepository.count();
        long active = loanRepository.countByStatus("ACTIVE");
        long overdue = loanRepository.countByStatus("OVERDUE");
        return new StatsSummaryDTO(books, authors, active, overdue);
    }

    public List<TopBookDTO> getTopBooks() {
        return loanRepository.findTopBooks(PageRequest.of(0, 5));
    }

    public List<OverdueLoanDTO> getOverdueLoans() {
        List<Loan> activeLoans = loanRepository.findByStatus("ACTIVE");
        return activeLoans.stream()
                .filter(l -> ChronoUnit.DAYS.between(l.getLoanDate(), LocalDate.now()) > 14) // Considera atrasado após 14 dias
                .map(l -> {
                    long days = ChronoUnit.DAYS.between(l.getLoanDate().plusDays(14), LocalDate.now());
                    return new OverdueLoanDTO(l.getId(), l.getBook().getTitle(), l.getBorrowerEmail(), days);
                }).collect(Collectors.toList());
    }
}